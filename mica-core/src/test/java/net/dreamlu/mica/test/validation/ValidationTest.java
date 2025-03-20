package net.dreamlu.mica.test.validation;

import jakarta.validation.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.executable.ExecutableValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.validation.constraints.RangeIn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Set;

/**
 * validation 单元测试
 *
 * @author dy
 */
class ValidationTest {

	private static final Validator validator;

	static {
		try (final ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}
	}

	private ExecutableValidator obtainExecutableValidator() {
		return validator.forExecutables();
	}

	/**
	 * 测试 {@link RangeIn}
	 */
	@Test
	void test1() {
		Set<ConstraintViolation<User>> constraintViolations = validator.validateValue(User.class, "sex", 3);
		Assertions.assertFalse(constraintViolations.isEmpty());
	}

	/**
	 * 测试方法上使用 {@link Valid}
	 */
	@Test
	void test2() throws NoSuchMethodException {
		// R中需包含Valid注解, 方法使用Valid注解
		R<User> result = useValidAnnotation();
		Method currMethod = this.getClass().getMethod("useValidAnnotation");
		Set<ConstraintViolation<ValidationTest>> constraintViolations = obtainExecutableValidator().validateReturnValue(this, currMethod, result);
		Assertions.assertFalse(constraintViolations.isEmpty());
	}

	/**
	 * 测试方法上不使用 {@link Valid}
	 */
	@Test
	void test3() throws NoSuchMethodException {
		// R中包含Valid注解, 方法不使用Valid注解
		R<User> result = notUseValidAnnotation();
		Method currMethod = this.getClass().getMethod("notUseValidAnnotation");
		Set<ConstraintViolation<ValidationTest>> constraintViolations = obtainExecutableValidator().validateReturnValue(this, currMethod, result);
		Assertions.assertTrue(constraintViolations.isEmpty());
	}

	@Valid
	public R<User> useValidAnnotation() {
		User user = new User("admin", "123456", 1, null);
		return R.success(user);
	}

	public R<User> notUseValidAnnotation() {
		User user = new User("admin", "123456", 1, null);
		return R.success(user);
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class User {

	@NotBlank
	@NotNull
	private String userName;

	@Size(min = 10, max = 20)
	@NotNull
	private String password;

	@RangeIn(value = "0, 1, 2", message = "性别错误 0--女 1--男 2--未知")
	private Integer sex;

	@DecimalMin(value = "10000")
	private BigDecimal salary;
}
