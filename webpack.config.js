var webpack = require('webpack');
var path = require('path');
const fs = require('fs');
const HtmlWebpackPlugin = require('html-webpack-plugin')

function getEntries () {
    return fs.readdirSync('./src/main/js/app/')
        .filter(
            (file) => file.match(/.*\.js$/)
        )
        .map((file) => {
            return {
                name: file.substring(0, file.length - 3),
                path: './src/main/js/app/' + file
            }
        }).reduce((memo, file) => {
            memo[file.name] = file.path
            return memo;
        }, {})
}

function getHtmlPlugins() {
    return fs.readdirSync('./src/main/js/app/')
        .filter(
            (file) => file.match(/.*\.js$/)
        )
        .map((file) => {
            var name = file.substring(0, file.length - 3);
            return new HtmlWebpackPlugin({
                filename: name + '.html',
                title: name,
                chunks: [ 'commons', name ],
                template: 'src/main/js/app/template.html'
            });
        });
}

module.exports = {
    entry: getEntries,
    output: {
        filename: '[name].js',
        path: path.join(__dirname, "src/main/webapp/app"),
    },
    resolve: {
        modules: [
            path.join(__dirname, "src/main/js"),
            "node_modules"
        ]
    },
    devtool: "source-map",
    module: {
        rules: [{
                test: /\.js$/,
                exclude: /(node_modules)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['env', 'react', 'stage-0'],
                        plugins: ["transform-class-properties", "transform-object-rest-spread", "syntax-async-functions", "transform-regenerator"]
                    }
                }
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: [
                    'file-loader'
                ]
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: [
                    'file-loader'
                ]
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            name: 'commons',
            filename: 'commons.js',
          }),
        ...getHtmlPlugins()
    ]
};