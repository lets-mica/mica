/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dreamlu.mica.activerecord.generator;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MappingKitGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.druid.DruidPlugin;
import net.dreamlu.mica.core.utils.StringPool;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * CodeGenerator
 *
 * @author L.cm
 */
public class CodeGenerator extends Generator {
	private final String outputDir;
	private final boolean openDir;

	private CodeGenerator(DataSource dataSource,
						  String baseModelPackageName,
						  String baseModelOutputDir,
						  String modelPackageName,
						  String modelOutputDir,
						  boolean openDir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		this.outputDir = modelOutputDir;
		this.openDir = openDir;
	}

	@Override
	public void generate() {
		super.generate();
		// 完成后打开
		if (openDir) {
			open(outputDir);
		}
	}

	public static CodeGeneratorBuilder create() {
		return new CodeGeneratorBuilder();
	}

	/**
	 * 打开文件或者目录
	 */
	private static void open(String outDir) {
		if (StrKit.notBlank(outDir)) {
			try {
				String osName = System.getProperty("os.name");
				if (osName != null) {
					if (osName.contains("Mac")) {
						Runtime.getRuntime().exec("open " + outDir);
					} else if (osName.contains("Windows")) {
						Runtime.getRuntime().exec("cmd /c start " + outDir);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getModelTemplatePath() {
		return StringPool.SLASH + CodeGenerator.class.getPackage().getName()
			.replace(StringPool.DOT, StringPool.SLASH)
			.concat("/model_template.jf");
	}

	public static class EmptyMappingKitGenerator extends MappingKitGenerator {
		private static final String DEFAULT_PKG = "net.dreamlu.mica";
		public static final EmptyMappingKitGenerator INSTANCE = new EmptyMappingKitGenerator();

		private EmptyMappingKitGenerator() {
			super(DEFAULT_PKG, DEFAULT_PKG);
		}

		@Override
		public void generate(List<TableMeta> tableMetas) {

		}
	}

	public static class CodeGeneratorBuilder {
		private String url;
		private String username;
		private String password;
		private String basePackageName;
		private String outputDir;
		private boolean openDir = false;

		public CodeGeneratorBuilder url(String url) {
			this.url = url;
			return this;
		}

		public CodeGeneratorBuilder username(String username) {
			this.username = username;
			return this;
		}

		public CodeGeneratorBuilder password(String password) {
			this.password = password;
			return this;
		}

		public CodeGeneratorBuilder basePackageName(String basePackageName) {
			this.basePackageName = basePackageName;
			return this;
		}

		public CodeGeneratorBuilder outputDir(String outputDir) {
			this.outputDir = outputDir;
			return this;
		}

		public CodeGeneratorBuilder openDir() {
			this.openDir = true;
			return this;
		}

		public CodeGenerator build() {
			Assert.hasText(url, "代码生成数据库 url 为空");
			Assert.hasText(username, "代码生成数据库 username 为空");
			Assert.hasText(password, "代码生成数据库 password 为空");
			Assert.hasText(basePackageName, "代码生成 basePackageName 为空");
			Assert.hasText(outputDir, "代码生成 outputDir 为空");
			String codeOutputDir = outputDir + "/src/main/java/" + basePackageName.replace(StringPool.DOT, StringPool.SLASH);
			// model 所使用的包名 (MappingKit 默认使用的包名)
			String modelPackageName = basePackageName + ".model";
			// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
			String modelOutputDir = codeOutputDir + "/model";
			// base model 所使用的包名
			String baseModelPackageName = modelPackageName + ".base";
			// base model 文件保存路径
			String baseModelOutputDir = modelOutputDir + "/base";
			// 创建生成器
			DataSource dataSource = getDataSource();
			CodeGenerator generator = new CodeGenerator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir, openDir);
			generator.setModelTemplate(CodeGenerator.getModelTemplatePath());
			generator.setDataDictionaryGenerator(new DataDictionaryGenerator(dataSource, modelOutputDir));
			generator.setMappingKitGenerator(EmptyMappingKitGenerator.INSTANCE);
			// 配置是否生成备注
			generator.setGenerateRemarks(true);
			// 设置是否生成字典文件
			generator.setGenerateDataDictionary(true);
			return generator;
		}

		private DataSource getDataSource() {
			DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
			druidPlugin.start();
			return druidPlugin.getDataSource();
		}
	}

}
