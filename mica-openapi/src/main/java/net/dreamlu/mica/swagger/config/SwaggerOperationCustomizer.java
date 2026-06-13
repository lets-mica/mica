package net.dreamlu.mica.swagger.config;

import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

/**
 * 统一生成 operationId（原 uniqueId）
 * 规则：类简单名_方法名
 */
public class SwaggerOperationCustomizer implements OperationCustomizer {

	@Override
	public Operation customize(Operation operation, HandlerMethod handlerMethod) {
		// 生成新的 operationId
		String className = handlerMethod.getBeanType().getSimpleName();
		String methodName = handlerMethod.getMethod().getName();
		String operationId = className + '_' + methodName;
		operation.setOperationId(operationId);
		return operation;
	}

}
