package net.juniorbl.jtoyracing.enums;

/**
 * @version 1.0 Jun 22, 2007
 * @author Carlos Luz Junior
 */
public enum ResourcesPath {

	MODELS_PATH {
		@Override
		public String toString() {
			return "resources/models/";
		}
	},

	AUDIO_PATH {
		@Override
		public String toString() {
			return "resources/audio/";
		}
	},

	IMAGES_PATH {
		@Override
		public String toString() {
			return "resources/images/";
		}
	},

	TEXTURE_PATH {
		@Override
		public String toString() {
			return "resources/textures/";
		}
	}
}
