package com.parousia.nowrunning.common;

public class ApplicationConstants {

	public static String LOGTAG = "NowRunning";
	public static String feed = "http://www.nowrunning.com/cgi-bin/rss/reviews_";

	public enum Language {
		TAMIL {
			public String toString() {
				return "tamil";
			}
		},
		MALAYALAM {
			public String toString() {
				return "malayalam";
			}
		},
		TELUGU {
			public String toString() {
				return "telugu";
			}
		},
		HINDI {
			public String toString() {
				return "hindi";
			}
		},
		KANNADA {
			public String toString() {
				return "kannada";
			}
		},
		OTHER {
			public String toString() {
				return "Others";
			}
		}

	};
}
