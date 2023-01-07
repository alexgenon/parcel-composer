const CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');


module.exports = {

    plugins: [
        new CopyWebpackPlugin({
            patterns: [
                {from: 'public'}
            ]
        }),
        new GenerateSW({
            swDest: 'sw.js'
        })
    ]
};
