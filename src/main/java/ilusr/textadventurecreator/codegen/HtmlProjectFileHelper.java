package ilusr.textadventurecreator.codegen;

public class HtmlProjectFileHelper extends WebProjectFileHelper {
	public final static String PACKAGEJSON = "{\r\n" +
			"  \"name\": \"%1$s\",\r\n" +
			"  \"version\": \"0.1.0\",\r\n" +
			"  \"private\": true,\r\n" +
			"  \"description\": \"%2$s\",\r\n" +
			"  \"main\": \"src/index.js\",\r\n" +
			"  \"dependencies\": {\r\n" +
			"    \"autoprefixer\": \"7.1.6\",\r\n" +
			"    \"babel-core\": \"6.26.0\",\r\n" +
			"    \"babel-eslint\": \"7.2.3\",\r\n" +
			"    \"babel-jest\": \"20.0.3\",\r\n" +
			"    \"babel-loader\": \"7.1.2\",\r\n" +
			"    \"babel-preset-react-app\": \"^3.1.1\",\r\n" +
			"    \"babel-runtime\": \"6.26.0\",\r\n" +
			"    \"case-sensitive-paths-webpack-plugin\": \"2.1.1\",\r\n" +
			"    \"chalk\": \"1.1.3\",\r\n" +
			"    \"css-loader\": \"0.28.7\",\r\n" +
			"    \"dotenv\": \"4.0.0\",\r\n" +
			"    \"dotenv-expand\": \"4.2.0\",\r\n" +
			"    \"electron-is-dev\": \"^0.3.0\",\r\n" +
			"    \"eslint\": \"4.10.0\",\r\n" +
			"    \"eslint-config-react-app\": \"^2.1.0\",\r\n" +
			"    \"eslint-loader\": \"1.9.0\",\r\n" +
			"    \"eslint-plugin-flowtype\": \"2.39.1\",\r\n" +
			"    \"eslint-plugin-import\": \"2.8.0\",\r\n" +
			"    \"eslint-plugin-jsx-a11y\": \"5.1.1\",\r\n" +
			"    \"eslint-plugin-react\": \"7.4.0\",\r\n" +
			"    \"extract-text-webpack-plugin\": \"3.0.2\",\r\n" +
			"    \"file-loader\": \"^1.1.11\",\r\n" +
			"    \"fs-extra\": \"3.0.1\",\r\n" +
			"    \"html-webpack-plugin\": \"2.29.0\",\r\n" +
			"    \"jest\": \"20.0.4\",\r\n" +
			"    \"object-assign\": \"4.1.1\",\r\n" +
			"    \"postcss-flexbugs-fixes\": \"3.2.0\",\r\n" +
			"    \"postcss-loader\": \"2.0.8\",\r\n" +
			"    \"promise\": \"8.0.1\",\r\n" +
			"    \"raf\": \"3.4.0\",\r\n" +
			"    \"raw-loader\": \"^0.5.1\",\r\n" +
			"    \"react\": \"^16.3.2\",\r\n" +
			"    \"react-dev-utils\": \"^5.0.1\",\r\n" +
			"    \"react-dom\": \"^16.3.2\",\r\n" +
			"    \"resolve\": \"1.6.0\",\r\n" +
			"    \"style-loader\": \"0.19.0\",\r\n" +
			"    \"sw-precache-webpack-plugin\": \"0.11.4\",\r\n" +
			"    \"text-adventure-lib\": \"https://github.com/JeffreyRiggle/text-adventure-lib\",\r\n" +
			"    \"url-loader\": \"0.6.2\",\r\n" +
			"    \"webpack\": \"3.8.1\",\r\n" +
			"    \"webpack-dev-server\": \"2.9.4\",\r\n" +
			"    \"webpack-manifest-plugin\": \"1.3.2\",\r\n" +
			"    \"whatwg-fetch\": \"2.0.3\"\r\n" +
			"  },\r\n" +
			"  \"scripts\": {\r\n" +
			"    \"start\": \"node scripts/start.js\",\r\n" +
			"    \"build\": \"node scripts/build.js\",\r\n" +
			"    \"test\": \"node scripts/test.js --env=jsdom\"\r\n" +
			"  },\r\n" +
			"  \"homepage\": \".\",\r\n" +
			"  \"author\": \"%3$s\",\r\n" +
			"  \"jest\": {\r\n" +
			"    \"collectCoverageFrom\": [\r\n" +
			"      \"src/**/*.{js,jsx,mjs}\"\r\n" +
			"    ],\r\n" +
			"    \"setupFiles\": [\r\n" +
			"      \"<rootDir>/config/polyfills.js\"\r\n" +
			"    ],\r\n" +
			"    \"testMatch\": [\r\n" +
			"      \"<rootDir>/src/**/__tests__/**/*.{js,jsx,mjs}\",\r\n" +
			"      \"<rootDir>/src/**/?(*.)(spec|test).{js,jsx,mjs}\"\r\n" +
			"    ],\r\n" +
			"    \"testEnvironment\": \"node\",\r\n" +
			"    \"testURL\": \"http://localhost\",\r\n" +
			"    \"transform\": {\r\n" +
			"      \"^.+\\\\.(js|jsx|mjs)$\": \"<rootDir>/node_modules/babel-jest\",\r\n" +
			"      \"^.+\\\\.css$\": \"<rootDir>/config/jest/cssTransform.js\",\r\n" +
			"      \"^(?!.*\\\\.(js|jsx|mjs|css|json)$)\": \"<rootDir>/config/jest/fileTransform.js\"\r\n" +
			"    },\r\n" +
			"    \"transformIgnorePatterns\": [\r\n" +
			"      \"[/\\\\\\\\]node_modules[/\\\\\\\\].+\\\\.(js|jsx|mjs)$\"\r\n" +
			"    ],\r\n" +
			"    \"moduleNameMapper\": {\r\n" +
			"      \"^react-native$\": \"react-native-web\"\r\n" +
			"    },\r\n" +
			"    \"moduleFileExtensions\": [\r\n" +
			"      \"web.js\",\r\n" +
			"      \"js\",\r\n" +
			"      \"json\",\r\n" +
			"      \"web.jsx\",\r\n" +
			"      \"jsx\",\r\n" +
			"      \"node\",\r\n" +
			"      \"mjs\"\r\n" +
			"    ]\r\n" +
			"  },\r\n" +
			"  \"babel\": {\r\n" +
			"    \"presets\": [\r\n" +
			"      \"react-app\"\r\n" +
			"    ]\r\n" +
			"  },\r\n" +
			"  \"eslintConfig\": {\r\n" +
			"    \"extends\": \"react-app\"\r\n" +
			"  }\r\n" +
			"}";
}
