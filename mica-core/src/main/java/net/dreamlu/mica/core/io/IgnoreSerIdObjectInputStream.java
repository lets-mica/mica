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

package net.dreamlu.mica.core.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 忽略序列化 id 的 jdk 对象序列化
 *
 * <p>
 * 参考：https://stackoverflow.com/questions/1816559/make-java-runtime-ignore-serialversionuids
 * </p>
 *
 * @author L.cm
 */
@Slf4j
public class IgnoreSerIdObjectInputStream extends ObjectInputStream {

    public IgnoreSerIdObjectInputStream(byte[] bytes) throws IOException {
        this(new ByteArrayInputStream(bytes));
    }

    public IgnoreSerIdObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        // initially streams descriptor
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();
        // the class in the local JVM that this descriptor represents.
        Class<?> localClass;
        try {
            localClass = Class.forName(resultClassDescriptor.getName());
        } catch (ClassNotFoundException e) {
            log.warn("No local class for " + resultClassDescriptor.getName());
            return resultClassDescriptor;
        }
        ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
        // only if class implements serializable
        if (localClassDescriptor != null) {
            long localSerId = localClassDescriptor.getSerialVersionUID();
            long streamSerId = resultClassDescriptor.getSerialVersionUID();
            // check for serialVersionUID mismatch.
            if (streamSerId != localSerId) {
                log.warn("Overriding serialized class {} version mismatch: local serialVersionUID = {} stream serialVersionUID = {}", localClass, localSerId, streamSerId);
                // Use local class descriptor for deserialization
                resultClassDescriptor = localClassDescriptor;
            }
        }
        return resultClassDescriptor;
    }

}
