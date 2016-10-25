var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: ['babel-polyfill',"./js/app.jsx"],
    output: {
        path: path.resolve(__dirname, "build"),
        filename: "bundle.js"
    },
    plugins: [new HtmlWebpackPlugin({
        title: 'Joggr.io!',
         template: 'index.ejx',
    })],
    resolve: {
        alias: {
            joggr: path.resolve(__dirname, 'js')
        },
        extensions: ['', '.js', '.jsx']
    },
    devtool: '#source-map',
    module: {
      loaders: [
        {
          test: /\.jsx?$/,
          loader: 'babel',
          query: {
            presets: [ 'react', 'es2015' ],
            compact: true,
            minified: true,
            comments: false,
            babelrc: false
          }
        }
      ]
    }
}

