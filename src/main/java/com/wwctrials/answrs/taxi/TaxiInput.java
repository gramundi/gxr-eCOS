package com.wwctrials.answrs.taxi;

public class TaxiInput {

	public static void run(String input) {
		System.out.println("TAXI INPUT: " + input);
		Taxi.getInstance().run(input);
	}
}
