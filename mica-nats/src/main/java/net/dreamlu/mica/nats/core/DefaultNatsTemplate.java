package net.dreamlu.mica.nats.core;

import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;

/**
 * 默认的 NatsTemplate
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class DefaultNatsTemplate implements NatsTemplate {
	private final Connection connection;



}
