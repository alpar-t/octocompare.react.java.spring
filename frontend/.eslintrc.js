module.exports = {
    "extends": [
        "airbnb",
    ],
    "settings": {
        "import/parser": "babel-eslint",
        "import/resolver": "webpack"
    },
    "plugins": [
        "react",
        "jsx-a11y",
        "import",
        "jasmine",
    ],
    "env": {
          "jasmine": true
    }
};
