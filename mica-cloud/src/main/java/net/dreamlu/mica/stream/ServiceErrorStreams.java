package net.dreamlu.mica.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 服务异常 Streams
 *
 * @author L.cm
 */
public interface ServiceErrorStreams {
	String INPUT = "service-error-in";
	String OUTPUT = "service-error-out";

	/**
	 * input
	 *
	 * @return SubscribableChannel
	 */
	@Input(INPUT)
	SubscribableChannel subscribablebChannel();

	/**
	 * output
	 *
	 * @return MessageChannel
	 */
	@Output(OUTPUT)
	MessageChannel messageChannel();
}
