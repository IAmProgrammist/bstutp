const MiniCssExtractPlugin = require('mini-css-extract-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');

module.exports = {
    mode: "development",
    plugins: [new MiniCssExtractPlugin(), new HtmlWebpackPlugin()],
    entry: './src/client/index.js',
    devServer: {
        static: {
            directory: path.join(__dirname, '/src/main/resources/static'),
        },
        historyApiFallback: true,
        compress: true,
        port: 9000
    },
    output: {
        filename: "build.js",
        // Это отвратительно, но это позволяет делать сервер максимально безболезненно
        path: __dirname + '/src/main/resources/static'
    },
    module: {
        rules: [
            {
                test: /\.m?js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: "babel-loader"
                }
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                type: 'asset/resource'
            },
            {
                test: /\.css$/i,
                use: ["style-loader", "css-loader"],
            }
        ]
    }
}
