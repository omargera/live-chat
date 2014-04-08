package com.livechat;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import com.livechat.gui.Game;

@Nonnull
public class App {
	private final String message;

	public App(String message) {
		this.message = checkNotNull(message,
				"Expected not null message argument.");
		
		new Game();
	}

	public String getMessage() {
		return message;
	}
}
