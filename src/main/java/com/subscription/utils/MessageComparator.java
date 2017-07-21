package com.subscription.utils;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.subscription.model.Message;

@Component
public class MessageComparator  implements Comparator<Message> {

	@Override
	public int compare(Message o1, Message o2) {
		if (o1 != null && o2 != null) {
			return o1.compareTo(o2);
		} else {
			return 0;
		}
	}

}
