var path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin')

module.exports = {
    entry: ['babel-polyfill', 'whatwg-fetch', 'normalize.css/normalize.css', './js/app.jsx'],
    output: {
        path: path.resolve(__dirname, 'build', 'dist'),
        filename: 'bundle.js'
    },
    plugins: [
	  new HtmlWebpackPlugin({
        title: 'Joggr.io!',
         template: 'index.ejx',
      }),
      new ExtractTextPlugin('bundle.css'),
    ],
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
          exclude: /node_modules/,
          loader: 'babel',
          query: {
            presets: [ 'react', 'es2015' ],
            compact: true,
            minified: true,
            comments: false,
            babelrc: false,
            cacheDirectory: '.webpack.cache',  
          }
        },
        {
          test: /\.css$/,
          loader: ExtractTextPlugin.extract('style-loader', 'css-loader')
        },
        {
          test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
          loader: 'url-loader?limit=10000&mimetype=application/font-woff'
        },
        {
          test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
          loader: 'file-loader'
        },  
      ]
    }
}

