package nyeblock.Core.ServerCoreTest.Misc;

import java.util.ArrayList;
import java.util.UUID;

import nyeblock.Core.ServerCoreTest.Main;

public class TextAnimation {
	private String[] messages;
	private int lastIndex = 0;
	
	public TextAnimation(Main mainInstance, ArrayList<String> list, double delay) {
		messages = list.toArray(new String[0]);
		
		mainInstance.getTimerInstance().createRunnableTimer("textAnimation_" + UUID.randomUUID(), delay, 0, new Runnable() {
			@Override
			public void run() {
				lastIndex++;
				
				if (messages.length-1 < lastIndex) {
					lastIndex = 0;
				}					
			}
		});
	}
	public String getMessage() {
		return messages[lastIndex];
	}
}