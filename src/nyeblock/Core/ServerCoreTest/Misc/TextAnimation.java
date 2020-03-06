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
//				System.out.println("Before: " + lastIndex);
				lastIndex++;
				
				if (messages.length-1 < lastIndex) {
					lastIndex = 0;
				}
//				System.out.println("After: " + lastIndex + " :: " + messages[lastIndex]);					
			}
		});
	}
	public String getMessage() {
		return messages[lastIndex];
	}
}