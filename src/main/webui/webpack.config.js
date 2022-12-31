const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');

// @see https://developers.google.com/web/tools/workbox/reference-docs/latest/module-workbox-webpack-plugin.InjectManifest
const WorkboxWebpackPlugin = require("workbox-webpack-plugin");

module.exports = {
    entry: './src/index.tsx',
    output: {
        filename: 'main.js',
        path: path.resolve(__dirname, 'build'),
    },
    plugins: [
        new CopyWebpackPlugin({
            patterns: [
                {from: 'public'}
            ]
        }),
    new WorkboxWebpackPlugin.InjectManifest({
        swSrc: "./src/src-sw.js",
        swDest: "sw.js",
    })
    ]
};
