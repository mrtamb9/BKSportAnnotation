(function () {
    /**
     * Gives you the base URL for the wurfldemi service dependent on the environment given.
     * If no environment is given then it'll try to get one from the blq object, or will
     * default to 'test'.
     *
     * @param {string} env The environment on which to call the wurfldemi service.
     * @returns {string} The base URL for the wurfldemi service.
     */
    function getServiceBaseURL(env) {
        var base;
        if (!env && typeof blq !== 'undefined' && blq.environment) {
            env = blq.environment();
        }
        if (env === 'sandbox') {
            if (document.cookie && /demi-service-use-sandbox=1/.test(document.cookie)) {
                base = 'http://pal.sandbox.dev.bbc.co.uk:8184';
            }
            else {
                env = 'test';
            }
        }
        if (! base) {
            base = 'http://open.' + (env || 'live') + '.bbc.co.uk';
        }
        return base + '/wurfldemi';
    }

    /**
     * @param {string} callback A string to use as the callback function name for a JSONP request.
     * @param {string} env A string representing the environment on which to call the wurfldemi service.
     * @returns {string} The URL of the useragent detection part of the wurfldemi service.
     */
    function getUseragentServiceURL(callback, env) {
        var base = getServiceBaseURL(env);
        return base + '/useragent.jsonp?callback=' + callback + '&ua=' + encodeURIComponent(navigator.userAgent);
    }

    /**
     * @param {string} callback A string to use as the callback function name for a JSONP request.
     * @param {string} env A string representing the environment on which to call the wurfldemi service.
     * @returns {string} The URL of the network detection part of the wurfldemi service.
     */
    function getNetworkServiceURL(env) {
        var base = getServiceBaseURL(env);
        return base + '/network.jsonp?callback={callback}';
    }

    function parseDimension(s) {
        var parts = (s || '').split(/x/);
        if (parts.length === 2) {
            return [parseInt(parts[0], 10), parseInt(parts[1], 10)];
        } else {
            return null;
        }
    }

    /**
     * @class Device
     * @classdesc An object containing information about the device accessing the page.
     */
    function Device(data) {
        this.data = data;
        this._display = parseDimension(data.display);
    }

    Device.prototype = {
        /**
         * @method Device#type
         * @desc Returns the type of the device e.g. 'mobile'|'desktop'|'tv'
         * @function
         * @public
         * @returns {string} The type of the device.
         */
        type: function() {
            return this.data.type !== '' ? this.data.type : null;
        },
        /**
         * @method Device#isRecognised
         * @desc Tests whether or not the device was matched.
         * @function
         * @public
         * @returns {boolean}
         */
        isRecognised: function() {
            return this.data.type !== '';
        },
        /**
         * @method Device#isDesktop
         * @desc Tests whether or not the device is a desktop.
         * @function
         * @public
         * @returns {boolean}
         */
        isDesktop: function() {
            return this.data.type === 'desktop';
        },
        /**
         * @method Device#isMobile
         * @desc Tests whether or not the device is a mobile.
         * @function
         * @public
         * @returns {boolean}
         */
        isMobile: function() {
            return this.data.type === 'mobile';
        },
        /**
         * @method Device#isTV
         * @desc Tests whether or not the device is a TV.
         * @function
         * @public
         * @returns {boolean}
         */
        isTV: function() {
            return this.data.type === 'tv';
        },
         /**
         * @method Device#isTablet
         * @desc Tests whether or not the device is a tablet.
         * @function
         * @public
         * @returns {boolean}
         */
        isTablet: function() {
            return this.data.type === 'tablet';
        },
       /**
         * @method Device#wurflId
         * @desc Returns the wurfl id of the device.
         * @function
         * @public
         * @returns {string}
         */
        wurflId: function() {
            return this.data.wurfl_id ? this.data.wurfl_id : null;
        },
        /**
         * @method Device#userAgent
         * @desc Returns the user agent string of the device.
         * @function
         * @public
         * @returns {string}
         */
        userAgent: function() {
            return this.data.user_agent ? this.data.user_agent : null;
        },
        /**
         * @method Device#brand
         * @desc Returns the brand of the device.
         * @function
         * @public
         * @returns {string}
         */
        brand: function() {
            return this.data.brand ? this.data.brand : null;
        },
        /**
         * @method Device#model
         * @desc Returns the model of the device.
         * @function
         * @public
         * @returns {string}
         */
        model: function() {
            return this.data.model ? this.data.model : null;
        },
        /**
         * @method Device#browser
         * @desc Returns the browser being used on the device.
         * @function
         * @public
         * @returns {string}
         */
        browser: function() {
            return this.data.browser ? this.data.browser : null;
        },
        /**
         * @method Device#os
         * @desc Returns the operating system being used on the device.
         * @function
         * @public
         * @returns {string}
         */
        os: function() {
            return this.data.os ? this.data.os : null;
        },
        /**
         * @method Device#osVersion
         * @desc Returns the version of the operating system being used on the device.
         * @function
         * @public
         * @returns {string}
         */
        osVersion: function() {
            return this.data.os_version ? this.data.os_version : null;
        },
        /**
         * @method Device#xhtmlLevel
         * @desc Returns the level of XHTML support on the device.
         * @function
         * @public
         * @returns {integer}
         */
        xhtmlLevel: function() {
            var level = this.data.xhtml_level;
            if (level) {
                return parseInt(level, 10);
            } else {
                return 0;
            }
        },
        /**
         * @method Device#navigation
         * @desc Returns the type of navigation being used on the device e.g. 'mouse'
         * @function
         * @public
         * @returns {string}
         */
        navigation: function() {
            return this.data.navigation ? this.data.navigation : null;
        },
        /**
         * @method Device#hasTouchscreen
         * @desc Tests whether the input type of the device is a touchscreen.
         * @function
         * @public
         * @returns {boolean}
         */
        hasTouchscreen: function() {
            return this.data.navigation === 'touchscreen';
        },
        /**
         * @method Device#hasStylus
         * @desc Tests whether the input type of the device is a stylus.
         * @function
         * @public
         * @returns {boolean}
         */
        hasStylus: function() {
            return this.data.navigation === 'stylus';
        },
        /**
         * @method Device#hasJoystick
         * @desc Tests whether the input type of the device is a joystick.
         * @function
         * @public
         * @returns {boolean}
         */
        hasJoystick: function() {
            return this.data.navigation === 'joystick';
        },
        /**
         * @method Device#hasClickwheel
         * @desc Tests whether the input type of the device is a clickwheel.
         * @function
         * @public
         * @returns {boolean}
         */
        hasClickwheel: function() {
            return this.data.navigation === 'clickwheel';
        },
        /**
         * @method Device#hasMouse
         * @desc Tests whether the input type of the device is a mouse.
         * @function
         * @public
         * @returns {boolean}
         */
        hasMouse: function() {
            return this.data.navigation === 'mouse';
        },
        /**
         * @method Device#display
         * @desc Returns the display data array for the device.
         * @function
         * @public
         * @returns {array}
         */
        display: function() {
            return this._display;
        },
        /**
         * @method Device#displayWidth
         * @desc Returns the display width for the device.
         * @function
         * @public
         * @returns {integer}
         */
        displayWidth: function() {
            return this._display ? this._display[0] : null;
        },
        /**
         * @method Device#displayHeight
         * @desc Returns the display height for the device.
         * @function
         * @public
         * @returns {integer}
         */
        displayHeight: function() {
            return this._display ? this._display[1] : null;
        },

        /**
         * @method Device#streamingMp4
         * @desc Checks if the device supports MP4 streaming
         * @function
         * @public
         * @returns {boolean}
         */
        streamingMp4: function() {
            return this.data.streaming_mp4 === 'true';
        },

        /**
         * @method Device#streaming3gpp
         * @desc Checks if the device supports 3GPP streaming
         * @function
         * @public
         * @returns {boolean}
         */
        streaming3gpp: function() {
            return this.data.streaming_3gpp === 'true';
        },

        /**
         * @method Device#mp3
         * @desc Checks if the device supports mp3
         * @function
         * @public
         * @returns {boolean}
         */
        mp3: function() {
            return this.data.mp3 === 'true';
        }

    };

    /**
     * @class Network
     * @classdesc An object containing information about the network that device accessing the page is using.
     */
    function Network(data){
        return {
            /**
             * @method Network#version
             * @desc Get the version of the wurfldemi service that we're contacting.
             * @returns {string} The wurfldemi service version number.
             * @function
             * @public
             */
            version: function() {
                return data.version;
            },
            /**
             * @method Network#isMobile
             * @desc Test whether the current request was made from a mobile network. Returns a boolean or null, if the network type could not be verified.
             * @returns {Boolean|null} Whether or not the device is on a mobile network, or null if that couldn't be decided.
             * @function
             * @public
             */
            isMobile: function() {
                return (data.is_mobile_network === undefined || this.error()) ? null : data.is_mobile_network;
            },
            /**
             * @method Network#error
             * @desc returns either an Error object or null, if an error has not occurred.
             * @returns {Error|null} Test whether or not an error occurred.
             * @function
             * @public
             */
            error: function() {
                if (data && data.is_mobile_network_error) {
                    return new Error('error getting network data: ' + data.is_mobile_network_error);
                }
                else if (data && data.is_mobile_network === undefined) {
                    return new Error('could not determine network');
                }
                return null;
            }
        }
    }

    function getUnusedCallbackName (prefix) {
        for (var i = 0; (prefix + i) in this; i++){
            ;
        }
        return prefix + i;
    }


    if (typeof define !== 'undefined') {
        var global = this;

        /**
         * @name demi
         * @public
         *
         * @desc Functions used to get device and network information.
         */
        define([getUseragentServiceURL('define')], function (data) {
            /*
             * @name {object} ns module namespace
             * @name {Array} callbacks Array to hold callback functions.
             * @name {DOMElement} callbackName current script tag that is pending
             * @name {string} callbackName name used to call back into the module.
             */
            var ns            = {},
                callbacks     = [],
                onReceive     = null,
                callbackName  = getUnusedCallbackName('_demi_mobile_network_cb_');

            /*
             * @name _addScriptTag
             * @desc Add a script tag into the head of the document. Give it the id contained in tagId.
             * @private
             * @function
             *
             * @param {string} src A URL to use as the 'src attribute' for the script tag.
             */
            ns._addScriptTag = function (urlTemplate, callbackName) {
                var head    = document.getElementsByTagName('head')[0],
                    script  = document.createElement('script');
                onReceive = function () {
                    head.removeChild(script);
                };
                script.type = 'text/javascript';
                script.src  = urlTemplate.replace(/{callback}/, callbackName);
                head.insertBefore(script, head.firstChild);
            };

            /*
             * @name callbackName
             * @desc Callback function for JSONP requests.
             * @private
             * @function
             *
             * @param {Object} data Information returned from service URL.
             * @function
             */
            global[callbackName] = function(data) {
                ns._runCallbacks(data);
                onReceive();
                onReceive = null;
            };

            /**
             * The device object.
             */
            ns.device = new Device(data);

            /**
             * @name getNetwork
             * @desc Get the mobile network info and run a callback when done.
             * @function
             * @public
             *
             * @param {function} callback Function to execute once the network information has been retrieved; this should take parameters for an error and the network object.
             */
            ns.getNetwork = function (callback) {
                callbacks.push(callback);
                if (! onReceive){
                    // If there is information about the connection in the navigator object then use that.
                    if (navigator.connection && navigator.connection.type != navigator.connection.UNKNOWN) {
                        var type = navigator.connection.type,
                            data = {};
                        data.is_mobile_network = (type == navigator.connection.CELL_3G || type == navigator.connection.CELL_2G);
                        data.is_mobile_network_error = false;
                        data.version = 'navigator';
                        ns._runCallbacks(data);
                    }
                    else {
                        ns._addScriptTag(getNetworkServiceURL(), callbackName);
                    }
                }
            };

            /*
             * @name _runCallbacks
             * @desc Takes the network data and runs the callbacks on it, after wrapping it in a Network object.
             * @private
             * @function
             *
             * @param {Object} data Information about the network, either from the browser or from the GeoIP service.
             * @function
             */
            ns._runCallbacks = function(data) {
                var network = new Network(data),
                    error = network.error();
                while (callbacks.length > 0) {
                    callbacks.pop()(error, network);
                }
            }

            return ns;
        });
    }

    // TODO old interface - to be deprecated
    if (typeof demi !== 'undefined') {
        var callbacks = [],
            isMobileNetworkCallbacks = [],
            device = null;

        demi._addScriptTag = function (src) {
            var headTag = document.getElementsByTagName('head')[0];
            var scriptTag = document.createElement('script');
            scriptTag.type = 'text/javascript';
            scriptTag.src = src;
            headTag.insertBefore(scriptTag, headTag.firstChild);
        };
        demi._receiveDevice = function(data) {
            device = new Device(data);
            var error = !device.isRecognised();
            while (callbacks.length > 0) {
                callbacks.pop()(error, device);
            }
            return device;
        };
        demi.getDevice = function (callback, env) {
            if (device !== null) {
                callback(!device.isRecognised(), device);
            } else if (callbacks.length > 0) {
                callbacks.push(callback);
            } else {
                callbacks.push(callback);
                demi._addScriptTag(getUseragentServiceURL('demi._receiveDevice', env));
            }
        };
        if (demi._loaded) demi._loaded();
    }
})();
