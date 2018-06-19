package ilusr.textadventurecreator.codegen;

public class ElectronProjectFileHelper {
	public final static String PACKAGEJSON = "{\r\n" +
			" \"name\": \"%1$s\",\r\n" +
			" \"version\": \"0.1.0\",\r\n" +
			" \"private\": true,\r\n" +
			" \"description\": \"%3$s\",\r\n" + 
			" \"main\": \"public/electron.js\",\r\n" +
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
			"  }," +
			"  \"scripts\": {\r\n" +
			"   \"start\": \"node scripts/start.js\",\r\n" +
			"   \"build\": \"node scripts/build.js\",\r\n" +
			"   \"test\": \"node scripts/test.js --env=jsdom\",\r\n" +
			"   \"electron-dev\": \"concurrently \\\"yarn start\\\" \\\"wait-on http://localhost:3000 && electron .\\\"\",\r\n" +
			"   \"pack\": \"build --dir\",\r\n" +
			"   \"dist\": \"npm run build && build\"\r\n" +
			" },\r\n" +
			" \"homepage\": \".\",\r\n" +
			" \"author\": \"%2$s\",\r\n" +
			" \"build\": {\r\n" +
			"   \"appId\": \"%2$s.%1$s\",\r\n" +
			"   \"productName\": \"%1$s\",\r\n" +
			"   \"copyright\": \"Copyright © year ${author}\",\r\n" +
			"   \"win\": {\r\n" +
			"     \"icon\": \"./public/fav.png\"\r\n" +
			"   },\r\n" +
			"   \"directories\": {\r\n" +
			"     \"buildResources\": \"public\"\r\n" +
			"   },\r\n" +
			"   \"extends\": \"react-cra\"\r\n" +
			" },\r\n" +
			" \"devDependencies\": {\r\n" +
			"   \"concurrently\": \"3.5.1\",\r\n" +
			"   \"electron\": \"1.8.6\",\r\n" +
			"   \"electron-builder\": \"20.15.1\",\r\n" +
			"   \"wait-on\": \"2.1.0\"\r\n" +
			" },\r\n" +
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
			"}\r\n";
	public final static String GITIGNORE = "# See https://help.github.com/ignore-files/ for more about ignoring files.\r\n" +
			"\r\n" +
			"# dependencies\r\n" +
			"/node_modules\r\n" +
			"\r\n" +
			"# testing\r\n" +
			"/coverage\r\n" +
			"\r\n" +
			"# production\r\n" +
			"/build\r\n" +
			"\r\n" +
			"# misc\r\n" +
			".DS_Store\r\n" +
			".env.local\r\n" +
			".env.development.local\r\n" +
			".env.test.local\r\n" +
			".env.production.local\r\n" +
			"\r\n" +
			"npm-debug.log*\r\n" +
			"yarn-debug.log*\r\n" +
			"yarn-error.log*\r\n";
	public final static String ELECTRONJS = "const electron = require('electron');\r\n" +
			"const app = electron.app;\r\n" +
			"const BrowserWindow = electron.BrowserWindow;\r\n" +
			"\r\n" +
			"const path = require('path');\r\n" +
			"const url = require('url');\r\n" +
			"const isDev = require('electron-is-dev');\r\n" +
			"\r\n" +
			"let mainWindow;\r\n" +
			"\r\n" +
			"function createWindow() {\r\n" +
			"  mainWindow = new BrowserWindow({width: 900, height: 680});\r\n" +
			"  mainWindow.setMenu(null);\r\n" +
			"  mainWindow.loadURL(isDev ? 'http://localhost:3000' : `file://${path.join(__dirname, '/index.html')}`);\r\n" +
			"  mainWindow.on('closed', () => mainWindow = null);\r\n" +
			"}\r\n" +
			"\r\n" +
			"app.on('ready', createWindow);\r\n" +
			"\r\n" +
			"app.on('window-all-closed', () => {\r\n" +
			"  if (process.platform !== 'darwin') {\r\n" +
			"    app.quit();\r\n" +
			"  }\r\n" +
			"});\r\n" +
			"\r\n" +
			"app.on('activate', () => {\r\n" +
			"  if (mainWindow === null) {\r\n" +
			"    createWindow();\r\n" +
			"  }\r\n" +
			"});";
	public final static String INDEXHTML = "<!DOCTYPE html>\r\n" +
			"<html lang=\"en\">\r\n" +
			"  <head>\r\n" +
			"    <meta charset=\"utf-8\">\r\n" +
			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n" +
			"    <meta name=\"theme-color\" content=\"#000000\">\r\n" +
			"    <!--\r\n" +
			"      manifest.json provides metadata used when your web app is added to the\r\n" +
			"      homescreen on Android. See https://developers.google.com/web/fundamentals/engage-and-retain/web-app-manifest/\r\n" +
			"    -->\r\n" +
			"    <link rel=\"manifest\" href=\"%%PUBLIC_URL%%/manifest.json\">\r\n" +
			"    <link rel=\"shortcut icon\" href=\"%%PUBLIC_URL%%/favicon%2$s\">\r\n" +
			"    <!--\r\n" +
			"      Notice the use of %%PUBLIC_URL%% in the tags above.\r\n" +
			"      It will be replaced with the URL of the `public` folder during the build.\r\n" +
			"      Only files inside the `public` folder can be referenced from the HTML.\r\n" +
			"\r\n" +
			"      Unlike \"/favicon.ico\" or \"favicon.ico\", \"%%PUBLIC_URL%%/favicon.ico\" will\r\n" +
			"      work correctly both with client-side routing and a non-root public URL.\r\n" +
			"      Learn how to configure a non-root public URL by running `npm run build`.\r\n" +
			"    -->\r\n" +
			"    <title>%1$s</title>\r\n" +
			"  </head>\r\n" +
			"  <body>\r\n" +
			"    <noscript>\r\n" +
			"      You need to enable JavaScript to run this app.\r\n" +
			"    </noscript>\r\n" +
			"    <div id=\"root\"></div>\r\n" +
			"    <!--\r\n" +
			"      This HTML file is a template.\r\n" +
			"      If you open it directly in the browser, you will see an empty page.\r\n" +
			"\r\n" +
			"      You can add webfonts, meta tags, or analytics to this file.\r\n" +
			"      The build step will place the bundled scripts into the <body> tag.\r\n" +
			"\r\n" +
			"      To begin the development, run `npm start` or `yarn start`.\r\n" +
			"      To create a production bundle, use `npm run build` or `yarn build`.\r\n" +
			"    -->\r\n" +
			"  </body>\r\n" +
			"</html>";
	public final static String MANIFESTJSON = "{\r\n" +
			"  \"short_name\": \"%1$s\",\r\n" +
			"  \"name\": \"%1$s\",\r\n" +
			"  \"icons\": [\r\n" +
			"    {\r\n" +
			"      \"src\": \"favicon%2$s\",\r\n" +
			"      \"sizes\": \"64x64 32x32 24x24 16x16\",\r\n" +
			"      \"type\": \"image/x-icon\"\r\n" +
			"    }\r\n" +
			"  ],\r\n" +
			"  \"start_url\": \"./index.html\",\r\n" +
			"  \"display\": \"standalone\",\r\n" +
			"  \"theme_color\": \"#000000\",\r\n" +
			"  \"background_color\": \"#ffffff\"\r\n" +
			"}\r\n";
	public final static String MAINPAGECSS = ".App {\r\n" +
			"  background: url('./%s') no-repeat;\r\n" +
			"  background-size: cover;\r\n" +
			"  min-height: 100vh;\r\n" +
			"  height: 100%%;\r\n" +
			"  min-width: 100%%;\r\n" +
			"  max-height: 100%%;\r\n" +
			"}\r\n" +
			"\r\n" +
			".Menu {\r\n" +
			"  position: absolute;\r\n" +
			"  top: 25%%;\r\n" +
			"  left: 15%%;\r\n" +
			"  width: 70%%;\r\n" +
			"  list-style: none;\r\n" +
			"}\r\n" +
			"\r\n" +
			".Menu li {\r\n" +
			"  margin-top: 40px;\r\n" +
			"  width: 100%%;\r\n" +
			"}\r\n" +
			"\r\n" +
			".Menu li button {\r\n" +
			"  background: rgba(60,179,113, .6);\r\n" +
			"  border: none;\r\n" +
			"  width: 100%%;\r\n" +
			"  height: 60px;\r\n" +
			"  font-weight: bolder;\r\n" +
			"  font-size: 24px;\r\n" +
			"}\r\n" +
			"\r\n" +
			".Menu li button:hover {\r\n" +
			"  background: rgba(60,179,213, .6);\r\n" +
			"}";
	public final static String APPJS = "import React, { Component } from 'react';\r\n" +
			"import './%1$s';\r\n" +
			"import { MainPage } from './MainPage';\r\n" +
			"import './images/assetLoader';\r\n" +
			"\r\n" +
			"class App extends Component {\r\n" +
			"  render() {\r\n" +
			"    return (\r\n" +
			"      <MainPage/>\r\n" +
			"    );\r\n" +
			"  }\r\n" +
			"}\r\n" +
			"\r\n" +
			"export default App;\r\n";
	public final static String GAMEMANAGERJS = "import React from 'react';\r\n" +
			"import ReactDOM from 'react-dom';\r\n" +
			"\r\n" +
			"import {TextAdventurePersistenceManager} from '../node_modules/text-adventure-lib/dist/main';\r\n" +
			"import '../node_modules/text-adventure-lib/dist/styles.css';\r\n" +
			"import gameFile from './%1$s';\r\n" +
			"import './images/assetLoader';\r\n" +
			"import { MainPage } from './MainPage';\r\n" +
			"\r\n" +
			"let persist = new TextAdventurePersistenceManager(gameFile);\r\n" +
			"\r\n" +
			"function reloadApp() {\r\n" +
			"    ReactDOM.render(<MainPage/>, document.getElementById('root'));\r\n" +
			"};\r\n" +
			"\r\n" +
			"function startGame() {\r\n" +
			"    persist.load();\r\n" +
			"    let game = persist.textAdventure.convertToGameStateManager(document.getElementById('root'));\r\n" +
			"    game.on(game.finishedEvent, reloadApp);\r\n" +
			"    game.start();\r\n" +
			"};\r\n" +
			"\r\n" +
			"export {\r\n" +
			"    startGame\r\n" +
			"};";
	public final static String MAINPAGEJS = "import React, { Component } from 'react';\r\n" +
			"import './MainPage.css';\r\n" +
			"import {startGame} from './gameManager';\r\n" +
			"\r\n" +
			"export class MainPage extends Component {\r\n" +
			"\r\n" +
			"  _loadGame() {\r\n" +
			"    startGame();\r\n" +
			"  }\r\n" +
			"\r\n" +
			"  render() {\r\n" +
			"    return (\r\n" +
			"      <div className=\"App\">\r\n" +
			"        <ul className=\"Menu\">\r\n" +
			"          <li><button onClick={this._loadGame.bind(this)}>Play</button></li>\r\n" +
			"          <li><button onClick={this._loadGame.bind(this)}>Load</button></li>\r\n" +
			"        </ul>\r\n" +
			"      </div>\r\n" +
			"    );\r\n" +
			"  }\r\n" +
			"}";
	public final static String APPTESTJS = "import React from 'react';\r\n" +
			"import ReactDOM from 'react-dom';\r\n" +
			"import App from './App';\r\n" +
			"\r\n" +
			"it('renders without crashing', () => {\r\n" +
			"  const div = document.createElement('div');\r\n" +
			"  ReactDOM.render(<App />, div);\r\n" +
			"  ReactDOM.unmountComponentAtNode(div);\r\n" +
			"});\r\n";
	public final static String INDEXCSS = "html {\r\n" +
			"  height: 100%;\r\n" +
			"  width: 100%;\r\n" +
			"  max-width: 100%;\r\n" +
			"  max-height: 100%;\r\n" +
			"}\r\n" +
			"\r\n" +
			"body {\r\n" +
			"  margin: 0;\r\n" +
			"  padding: 0;\r\n" +
			"  font-family: sans-serif;\r\n" +
			"  height: 100%;\r\n" +
			"  width: 100%;\r\n" +
			"  max-width: 100%;\r\n" +
			"  max-height: 100%;\r\n" +
			"}\r\n" +
			"\r\n" +
			"#root {\r\n" +
			"  height: 100%;\r\n" +
			"  width: 100%;\r\n" +
			"  max-width: 100%;\r\n" +
			"  max-height: 100%;\r\n" +
			"}\r\n";
	public final static String INDEXJS = "import React from 'react';\r\n" +
			"import ReactDOM from 'react-dom';\r\n" +
			"import './index.css';\r\n" +
			"import App from './App';\r\n" +
			"import registerServiceWorker from './registerServiceWorker';\r\n" +
			"\r\n" +
			"ReactDOM.render(<App />, document.getElementById('root'));\r\n" +
			"registerServiceWorker();\r\n";
	public final static String REGISTERSERVICEWORKERJS = "// In production, we register a service worker to serve assets from local cache.\r\n" +
			"\r\n" +
			"// This lets the app load faster on subsequent visits in production, and gives\r\n" +
			"// it offline capabilities. However, it also means that developers (and users)\r\n" +
			"// will only see deployed updates on the \"N+1\" visit to a page, since previously\r\n" +
			"// cached resources are updated in the background.\r\n" +
			"\r\n" +
			"// To learn more about the benefits of this model, read https://goo.gl/KwvDNy.\r\n" +
			"// This link also includes instructions on opting out of this behavior.\r\n" +
			"\r\n" +
			"const isLocalhost = Boolean(\r\n" +
			"  window.location.hostname === 'localhost' ||\r\n" +
			"    // [::1] is the IPv6 localhost address.\r\n" +
			"    window.location.hostname === '[::1]' ||\r\n" +
			"    // 127.0.0.1/8 is considered localhost for IPv4.\r\n" +
			"    window.location.hostname.match(\r\n" +
			"      /^127(?:\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/\r\n" +
			"    )\r\n" +
			");\r\n" +
			"\r\n" +
			"export default function register() {\r\n" +
			"  if (process.env.NODE_ENV === 'production' && 'serviceWorker' in navigator) {\r\n" +
			"    // The URL constructor is available in all browsers that support SW.\r\n" +
			"    const publicUrl = new URL(process.env.PUBLIC_URL, window.location);\r\n" +
			"    if (publicUrl.origin !== window.location.origin) {\r\n" +
			"      // Our service worker won't work if PUBLIC_URL is on a different origin\r\n" +
			"      // from what our page is served on. This might happen if a CDN is used to\r\n" +
			"      // serve assets; see https://github.com/facebookincubator/create-react-app/issues/2374\r\n" +
			"      return;\r\n" +
			"    }\r\n" +
			"\r\n" +
			"    window.addEventListener('load', () => {\r\n" +
			"      const swUrl = `${process.env.PUBLIC_URL}/service-worker.js`;\r\n" +
			"\r\n" +
			"      if (isLocalhost) {\r\n" +
			"        // This is running on localhost. Lets check if a service worker still exists or not.\r\n" +
			"        checkValidServiceWorker(swUrl);\r\n" +
			"\r\n" +
			"        // Add some additional logging to localhost, pointing developers to the\r\n" +
			"        // service worker/PWA documentation.\r\n" +
			"        navigator.serviceWorker.ready.then(() => {\r\n" +
			"          console.log(\r\n" +
			"            'This web app is being served cache-first by a service ' +\r\n" +
			"              'worker. To learn more, visit https://goo.gl/SC7cgQ'\r\n" +
			"          );\r\n" +
			"        });\r\n" +
			"      } else {\r\n" +
			"        // Is not local host. Just register service worker\r\n" +
			"        registerValidSW(swUrl);\r\n" +
			"      }\r\n" +
			"    });\r\n" +
			"  }\r\n" +
			"}\r\n" +
			"\r\n" +
			"function registerValidSW(swUrl) {\r\n" +
			"  navigator.serviceWorker\r\n" +
			"    .register(swUrl)\r\n" +
			"    .then(registration => {\r\n" +
			"      registration.onupdatefound = () => {\r\n" +
			"        const installingWorker = registration.installing;\r\n" +
			"        installingWorker.onstatechange = () => {\r\n" +
			"          if (installingWorker.state === 'installed') {\r\n" +
			"            if (navigator.serviceWorker.controller) {\r\n" +
			"              // At this point, the old content will have been purged and\r\n" +
			"              // the fresh content will have been added to the cache.\r\n" +
			"              // It's the perfect time to display a \"New content is\r\n" +
			"              // available; please refresh.\" message in your web app.\r\n" +
			"              console.log('New content is available; please refresh.');\r\n" +
			"            } else {\r\n" +
			"              // At this point, everything has been precached.\r\n" +
			"              // It's the perfect time to display a\r\n" +
			"              // \"Content is cached for offline use.\" message.\r\n" +
			"              console.log('Content is cached for offline use.');\r\n" +
			"            }\r\n" +
			"          }\r\n" +
			"        };\r\n" +
			"      };\r\n" +
			"    })\r\n" +
			"    .catch(error => {\r\n" +
			"      console.error('Error during service worker registration:', error);\r\n" +
			"    });\r\n" +
			"}\r\n" +
			"\r\n" +
			"function checkValidServiceWorker(swUrl) {\r\n" +
			"  // Check if the service worker can be found. If it can't reload the page.\r\n" +
			"  fetch(swUrl)\r\n" +
			"    .then(response => {\r\n" +
			"      // Ensure service worker exists, and that we really are getting a JS file.\r\n" +
			"      if (\r\n" +
			"        response.status === 404 ||\r\n" +
			"        response.headers.get('content-type').indexOf('javascript') === -1\r\n" +
			"      ) {\r\n" +
			"        // No service worker found. Probably a different app. Reload the page.\r\n" +
			"        navigator.serviceWorker.ready.then(registration => {\r\n" +
			"          registration.unregister().then(() => {\r\n" +
			"            window.location.reload();\r\n" +
			"          });\r\n" +
			"        });\r\n" +
			"      } else {\r\n" +
			"        // Service worker found. Proceed as normal.\r\n" +
			"        registerValidSW(swUrl);\r\n" +
			"      }\r\n" +
			"    })\r\n" +
			"    .catch(() => {\r\n" +
			"      console.log(\r\n" +
			"        'No internet connection found. App is running in offline mode.'\r\n" +
			"      );\r\n" +
			"    });\r\n" +
			"}\r\n" +
			"\r\n" +
			"export function unregister() {\r\n" +
			"  if ('serviceWorker' in navigator) {\r\n" +
			"    navigator.serviceWorker.ready.then(registration => {\r\n" +
			"      registration.unregister();\r\n" +
			"    });\r\n" +
			"  }\r\n" +
			"}\r\n" +
			"\r\n";
}
