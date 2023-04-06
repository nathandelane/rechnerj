package com.github.nathandelane.math.rechnerj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static com.github.nathandelane.math.rechnerj.Evaluation.evaluate;
import static com.github.nathandelane.math.rechnerj.Evaluation.rpnOrderTokens;
import static com.github.nathandelane.math.rechnerj.Logger.logError;
import static com.github.nathandelane.math.rechnerj.Tokens.*;

public class ApplicationMain {

	private static void out(final Object value) {
		System.out.println(value);
	}

	private static void prompt() {
		System.out.print("(:q,token) > ");
	}

	private static boolean handleCommandsAndContinue(final String input) {
		boolean shouldContinue = false;

		if (input.trim().equalsIgnoreCase(":q")) {
			isRunning = false;
			shouldContinue = true;
		}

		return shouldContinue;
	}

	private static void processInput(final InputStream inputStream) {
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
			while (isRunning) {
				out("");
				prompt();

				final String input = in.readLine();

				if (handleCommandsAndContinue(input)) continue;

				final List<Token> myTokens = tokenizeString(input);

				if (!myTokens.isEmpty()) {
					final List<Token> rpn = rpnOrderTokens(myTokens);
					final Token result = evaluate(rpn);
					final String resultValue = result.value;

					out(resultValue);

					myTokens.clear();
				}
			}
		} catch (final IOException | ArithmeticException e) {
			logError(e.getMessage());
		}
	}

	private static boolean isRunning;

	public static void main(final String[] args) {
		isRunning = true;

		processInput(System.in);
	}

}
