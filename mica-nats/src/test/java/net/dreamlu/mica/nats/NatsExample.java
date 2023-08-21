package net.dreamlu.mica.nats;

import io.nats.client.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * nats 测试
 */
public class NatsExample {

	public static void main(String[] args) {
		Options options = new Options.Builder()
			.server(Options.DEFAULT_URL)
			.build();
		try {
			Connection nc = Nats.connect(options);
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					nc.publish("subject", "Hello, NATS!".getBytes());
				}
			}, 3000, 3000);
			Dispatcher dispatcher = nc.createDispatcher(msg -> {
                byte[] bytes = msg.getData();
                System.out.println(new String(bytes));
            });
			dispatcher.subscribe("subject");
			Thread.sleep(10000L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
