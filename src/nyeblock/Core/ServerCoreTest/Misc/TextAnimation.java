package nyeblock.Core.ServerCoreTest.Misc;

import java.util.ArrayList;

public class TextAnimation {
	private String name;
	private String[] messages;
	private Integer interval;
	private int lastIndex = 0;
	private long lastTime = System.currentTimeMillis();
	
	public TextAnimation(String name, ArrayList<String> list, Integer interval) {
		this.name = name;
		this.messages = list.toArray(new String[0]);
		this.interval = interval;
	}
	public String getMessage() {
		String text;
		
		if (interval != null) {			
			if ((System.currentTimeMillis()-lastTime) >= interval) {
				lastIndex++;
				
				if (messages.length-1 < lastIndex) {
					lastIndex = 0;
				}
				text = messages[lastIndex];
				lastTime = System.currentTimeMillis();
			} else {
				text = messages[lastIndex];
			}
		} else {
			lastIndex++;
			
			if (messages.length-1 < lastIndex) {
				lastIndex = 0;
			}
			text = messages[lastIndex];
			lastTime = System.currentTimeMillis();
		}
		return text;
	}
	public String getName() {
		return name;
	}
}