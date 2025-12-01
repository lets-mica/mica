package net.dreamlu.mica.redis.config;

import tools.jackson.databind.DatabindContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * redis jackson 多态类型验证器
 *
 * @author L.cm
 */
public class RedisJackosnPolymorphicTypeValidator extends PolymorphicTypeValidator {
	public final static PolymorphicTypeValidator INSTANCE = new RedisJackosnPolymorphicTypeValidator();

	@Override
	public Validity validateBaseType(DatabindContext context, JavaType baseType) {
		return Validity.INDETERMINATE;
	}

	@Override
	public Validity validateSubClassName(DatabindContext context, JavaType baseType, String subClassName) {
		return Validity.ALLOWED;
	}

	@Override
	public Validity validateSubType(DatabindContext context, JavaType baseType, JavaType subType) {
		return Validity.ALLOWED;
	}

}
