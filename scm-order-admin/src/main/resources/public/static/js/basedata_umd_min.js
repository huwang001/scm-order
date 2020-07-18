(function (t, e) {
    "object" === typeof exports && "object" === typeof module ? module.exports = e() : "function" === typeof define && define.amd ? define([], e) : "object" === typeof exports ? exports["basedata"] = e() : t["basedata"] = e()
})("undefined" !== typeof self ? self : this, function () {
    return function (t) {
        var e = {};

        function n(i) {
            if (e[i]) return e[i].exports;
            var o = e[i] = {i: i, l: !1, exports: {}};
            return t[i].call(o.exports, o, o.exports, n), o.l = !0, o.exports
        }

        return n.m = t, n.c = e, n.d = function (t, e, i) {
            n.o(t, e) || Object.defineProperty(t, e, {enumerable: !0, get: i})
        }, n.r = function (t) {
            "undefined" !== typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(t, "__esModule", {value: !0})
        }, n.t = function (t, e) {
            if (1 & e && (t = n(t)), 8 & e) return t;
            if (4 & e && "object" === typeof t && t && t.__esModule) return t;
            var i = Object.create(null);
            if (n.r(i), Object.defineProperty(i, "default", {
                enumerable: !0,
                value: t
            }), 2 & e && "string" != typeof t) for (var o in t) n.d(i, o, function (e) {
                return t[e]
            }.bind(null, o));
            return i
        }, n.n = function (t) {
            var e = t && t.__esModule ? function () {
                return t["default"]
            } : function () {
                return t
            };
            return n.d(e, "a", e), e
        }, n.o = function (t, e) {
            return Object.prototype.hasOwnProperty.call(t, e)
        }, n.p = "", n(n.s = "fb15")
    }({
        "01f9": function (t, e, n) {
            "use strict";
            var i = n("2d00"), o = n("5ca1"), a = n("2aba"), r = n("32e9"), l = n("84f2"), c = n("41a0"), s = n("7f20"),
                u = n("38fd"), f = n("2b4c")("iterator"), p = !([].keys && "next" in [].keys()), h = "@@iterator",
                d = "keys", b = "values", y = function () {
                    return this
                };
            t.exports = function (t, e, n, m, v, g, S) {
                c(n, e, m);
                var C, T, x, k = function (t) {
                        if (!p && t in w) return w[t];
                        switch (t) {
                            case d:
                                return function () {
                                    return new n(this, t)
                                };
                            case b:
                                return function () {
                                    return new n(this, t)
                                }
                        }
                        return function () {
                            return new n(this, t)
                        }
                    }, _ = e + " Iterator", L = v == b, O = !1, w = t.prototype, U = w[f] || w[h] || v && w[v],
                    j = U || k(v), P = v ? L ? k("entries") : j : void 0, E = "Array" == e && w.entries || U;
                if (E && (x = u(E.call(new t)), x !== Object.prototype && x.next && (s(x, _, !0), i || "function" == typeof x[f] || r(x, f, y))), L && U && U.name !== b && (O = !0, j = function () {
                    return U.call(this)
                }), i && !S || !p && !O && w[f] || r(w, f, j), l[e] = j, l[_] = y, v) if (C = {
                    values: L ? j : k(b),
                    keys: g ? j : k(d),
                    entries: P
                }, S) for (T in C) T in w || a(w, T, C[T]); else o(o.P + o.F * (p || O), e, C);
                return C
            }
        }, "0d58": function (t, e, n) {
            var i = n("ce10"), o = n("e11e");
            t.exports = Object.keys || function (t) {
                return i(t, o)
            }
        }, 1495: function (t, e, n) {
            var i = n("86cc"), o = n("cb7c"), a = n("0d58");
            t.exports = n("9e1e") ? Object.defineProperties : function (t, e) {
                o(t);
                var n, r = a(e), l = r.length, c = 0;
                while (l > c) i.f(t, n = r[c++], e[n]);
                return t
            }
        }, "230e": function (t, e, n) {
            var i = n("d3f4"), o = n("7726").document, a = i(o) && i(o.createElement);
            t.exports = function (t) {
                return a ? o.createElement(t) : {}
            }
        }, "2aba": function (t, e, n) {
            var i = n("7726"), o = n("32e9"), a = n("69a8"), r = n("ca5a")("src"), l = n("fa5b"), c = "toString",
                s = ("" + l).split(c);
            n("8378").inspectSource = function (t) {
                return l.call(t)
            }, (t.exports = function (t, e, n, l) {
                var c = "function" == typeof n;
                c && (a(n, "name") || o(n, "name", e)), t[e] !== n && (c && (a(n, r) || o(n, r, t[e] ? "" + t[e] : s.join(String(e)))), t === i ? t[e] = n : l ? t[e] ? t[e] = n : o(t, e, n) : (delete t[e], o(t, e, n)))
            })(Function.prototype, c, function () {
                return "function" == typeof this && this[r] || l.call(this)
            })
        }, "2aeb": function (t, e, n) {
            var i = n("cb7c"), o = n("1495"), a = n("e11e"), r = n("613b")("IE_PROTO"), l = function () {
            }, c = "prototype", s = function () {
                var t, e = n("230e")("iframe"), i = a.length, o = "<", r = ">";
                e.style.display = "none", n("fab2").appendChild(e), e.src = "javascript:", t = e.contentWindow.document, t.open(), t.write(o + "script" + r + "document.F=Object" + o + "/script" + r), t.close(), s = t.F;
                while (i--) delete s[c][a[i]];
                return s()
            };
            t.exports = Object.create || function (t, e) {
                var n;
                return null !== t ? (l[c] = i(t), n = new l, l[c] = null, n[r] = t) : n = s(), void 0 === e ? n : o(n, e)
            }
        }, "2b4c": function (t, e, n) {
            var i = n("5537")("wks"), o = n("ca5a"), a = n("7726").Symbol, r = "function" == typeof a,
                l = t.exports = function (t) {
                    return i[t] || (i[t] = r && a[t] || (r ? a : o)("Symbol." + t))
                };
            l.store = i
        }, "2d00": function (t, e) {
            t.exports = !1
        }, "2d95": function (t, e) {
            var n = {}.toString;
            t.exports = function (t) {
                return n.call(t).slice(8, -1)
            }
        }, "32e9": function (t, e, n) {
            var i = n("86cc"), o = n("4630");
            t.exports = n("9e1e") ? function (t, e, n) {
                return i.f(t, e, o(1, n))
            } : function (t, e, n) {
                return t[e] = n, t
            }
        }, "38fd": function (t, e, n) {
            var i = n("69a8"), o = n("4bf8"), a = n("613b")("IE_PROTO"), r = Object.prototype;
            t.exports = Object.getPrototypeOf || function (t) {
                return t = o(t), i(t, a) ? t[a] : "function" == typeof t.constructor && t instanceof t.constructor ? t.constructor.prototype : t instanceof Object ? r : null
            }
        }, "41a0": function (t, e, n) {
            "use strict";
            var i = n("2aeb"), o = n("4630"), a = n("7f20"), r = {};
            n("32e9")(r, n("2b4c")("iterator"), function () {
                return this
            }), t.exports = function (t, e, n) {
                t.prototype = i(r, {next: o(1, n)}), a(t, e + " Iterator")
            }
        }, 4588: function (t, e) {
            var n = Math.ceil, i = Math.floor;
            t.exports = function (t) {
                return isNaN(t = +t) ? 0 : (t > 0 ? i : n)(t)
            }
        }, 4630: function (t, e) {
            t.exports = function (t, e) {
                return {enumerable: !(1 & t), configurable: !(2 & t), writable: !(4 & t), value: e}
            }
        }, "4bf8": function (t, e, n) {
            var i = n("be13");
            t.exports = function (t) {
                return Object(i(t))
            }
        }, 5537: function (t, e, n) {
            var i = n("8378"), o = n("7726"), a = "__core-js_shared__", r = o[a] || (o[a] = {});
            (t.exports = function (t, e) {
                return r[t] || (r[t] = void 0 !== e ? e : {})
            })("versions", []).push({
                version: i.version,
                mode: n("2d00") ? "pure" : "global",
                copyright: "© 2019 Denis Pushkarev (zloirock.ru)"
            })
        }, "5ca1": function (t, e, n) {
            var i = n("7726"), o = n("8378"), a = n("32e9"), r = n("2aba"), l = n("9b43"), c = "prototype",
                s = function (t, e, n) {
                    var u, f, p, h, d = t & s.F, b = t & s.G, y = t & s.S, m = t & s.P, v = t & s.B,
                        g = b ? i : y ? i[e] || (i[e] = {}) : (i[e] || {})[c], S = b ? o : o[e] || (o[e] = {}),
                        C = S[c] || (S[c] = {});
                    for (u in b && (n = e), n) f = !d && g && void 0 !== g[u], p = (f ? g : n)[u], h = v && f ? l(p, i) : m && "function" == typeof p ? l(Function.call, p) : p, g && r(g, u, p, t & s.U), S[u] != p && a(S, u, h), m && C[u] != p && (C[u] = p)
                };
            i.core = o, s.F = 1, s.G = 2, s.S = 4, s.P = 8, s.B = 16, s.W = 32, s.U = 64, s.R = 128, t.exports = s
        }, "613b": function (t, e, n) {
            var i = n("5537")("keys"), o = n("ca5a");
            t.exports = function (t) {
                return i[t] || (i[t] = o(t))
            }
        }, "626a": function (t, e, n) {
            var i = n("2d95");
            t.exports = Object("z").propertyIsEnumerable(0) ? Object : function (t) {
                return "String" == i(t) ? t.split("") : Object(t)
            }
        }, 6821: function (t, e, n) {
            var i = n("626a"), o = n("be13");
            t.exports = function (t) {
                return i(o(t))
            }
        }, "69a8": function (t, e) {
            var n = {}.hasOwnProperty;
            t.exports = function (t, e) {
                return n.call(t, e)
            }
        }, "6a99": function (t, e, n) {
            var i = n("d3f4");
            t.exports = function (t, e) {
                if (!i(t)) return t;
                var n, o;
                if (e && "function" == typeof (n = t.toString) && !i(o = n.call(t))) return o;
                if ("function" == typeof (n = t.valueOf) && !i(o = n.call(t))) return o;
                if (!e && "function" == typeof (n = t.toString) && !i(o = n.call(t))) return o;
                throw TypeError("Can't convert object to primitive value")
            }
        }, 7726: function (t, e) {
            var n = t.exports = "undefined" != typeof window && window.Math == Math ? window : "undefined" != typeof self && self.Math == Math ? self : Function("return this")();
            "number" == typeof __g && (__g = n)
        }, "77f1": function (t, e, n) {
            var i = n("4588"), o = Math.max, a = Math.min;
            t.exports = function (t, e) {
                return t = i(t), t < 0 ? o(t + e, 0) : a(t, e)
            }
        }, "79e5": function (t, e) {
            t.exports = function (t) {
                try {
                    return !!t()
                } catch (e) {
                    return !0
                }
            }
        }, "7f20": function (t, e, n) {
            var i = n("86cc").f, o = n("69a8"), a = n("2b4c")("toStringTag");
            t.exports = function (t, e, n) {
                t && !o(t = n ? t : t.prototype, a) && i(t, a, {configurable: !0, value: e})
            }
        }, "7f7f": function (t, e, n) {
            var i = n("86cc").f, o = Function.prototype, a = /^\s*function ([^ (]*)/, r = "name";
            r in o || n("9e1e") && i(o, r, {
                configurable: !0, get: function () {
                    try {
                        return ("" + this).match(a)[1]
                    } catch (t) {
                        return ""
                    }
                }
            })
        }, 8378: function (t, e) {
            var n = t.exports = {version: "2.6.6"};
            "number" == typeof __e && (__e = n)
        }, "84f2": function (t, e) {
            t.exports = {}
        }, "86cc": function (t, e, n) {
            var i = n("cb7c"), o = n("c69a"), a = n("6a99"), r = Object.defineProperty;
            e.f = n("9e1e") ? Object.defineProperty : function (t, e, n) {
                if (i(t), e = a(e, !0), i(n), o) try {
                    return r(t, e, n)
                } catch (l) {
                }
                if ("get" in n || "set" in n) throw TypeError("Accessors not supported!");
                return "value" in n && (t[e] = n.value), t
            }
        }, "9b43": function (t, e, n) {
            var i = n("d8e8");
            t.exports = function (t, e, n) {
                if (i(t), void 0 === e) return t;
                switch (n) {
                    case 1:
                        return function (n) {
                            return t.call(e, n)
                        };
                    case 2:
                        return function (n, i) {
                            return t.call(e, n, i)
                        };
                    case 3:
                        return function (n, i, o) {
                            return t.call(e, n, i, o)
                        }
                }
                return function () {
                    return t.apply(e, arguments)
                }
            }
        }, "9c6c": function (t, e, n) {
            var i = n("2b4c")("unscopables"), o = Array.prototype;
            void 0 == o[i] && n("32e9")(o, i, {}), t.exports = function (t) {
                o[i][t] = !0
            }
        }, "9def": function (t, e, n) {
            var i = n("4588"), o = Math.min;
            t.exports = function (t) {
                return t > 0 ? o(i(t), 9007199254740991) : 0
            }
        }, "9e1e": function (t, e, n) {
            t.exports = !n("79e5")(function () {
                return 7 != Object.defineProperty({}, "a", {
                    get: function () {
                        return 7
                    }
                }).a
            })
        }, ac6a: function (t, e, n) {
            for (var i = n("cadf"), o = n("0d58"), a = n("2aba"), r = n("7726"), l = n("32e9"), c = n("84f2"), s = n("2b4c"), u = s("iterator"), f = s("toStringTag"), p = c.Array, h = {
                CSSRuleList: !0,
                CSSStyleDeclaration: !1,
                CSSValueList: !1,
                ClientRectList: !1,
                DOMRectList: !1,
                DOMStringList: !1,
                DOMTokenList: !0,
                DataTransferItemList: !1,
                FileList: !1,
                HTMLAllCollection: !1,
                HTMLCollection: !1,
                HTMLFormElement: !1,
                HTMLSelectElement: !1,
                MediaList: !0,
                MimeTypeArray: !1,
                NamedNodeMap: !1,
                NodeList: !0,
                PaintRequestList: !1,
                Plugin: !1,
                PluginArray: !1,
                SVGLengthList: !1,
                SVGNumberList: !1,
                SVGPathSegList: !1,
                SVGPointList: !1,
                SVGStringList: !1,
                SVGTransformList: !1,
                SourceBufferList: !1,
                StyleSheetList: !0,
                TextTrackCueList: !1,
                TextTrackList: !1,
                TouchList: !1
            }, d = o(h), b = 0; b < d.length; b++) {
                var y, m = d[b], v = h[m], g = r[m], S = g && g.prototype;
                if (S && (S[u] || l(S, u, p), S[f] || l(S, f, m), c[m] = p, v)) for (y in i) S[y] || a(S, y, i[y], !0)
            }
        }, be13: function (t, e) {
            t.exports = function (t) {
                if (void 0 == t) throw TypeError("Can't call method on  " + t);
                return t
            }
        }, c366: function (t, e, n) {
            var i = n("6821"), o = n("9def"), a = n("77f1");
            t.exports = function (t) {
                return function (e, n, r) {
                    var l, c = i(e), s = o(c.length), u = a(r, s);
                    if (t && n != n) {
                        while (s > u) if (l = c[u++], l != l) return !0
                    } else for (; s > u; u++) if ((t || u in c) && c[u] === n) return t || u || 0;
                    return !t && -1
                }
            }
        }, c69a: function (t, e, n) {
            t.exports = !n("9e1e") && !n("79e5")(function () {
                return 7 != Object.defineProperty(n("230e")("div"), "a", {
                    get: function () {
                        return 7
                    }
                }).a
            })
        }, ca5a: function (t, e) {
            var n = 0, i = Math.random();
            t.exports = function (t) {
                return "Symbol(".concat(void 0 === t ? "" : t, ")_", (++n + i).toString(36))
            }
        }, cadf: function (t, e, n) {
            "use strict";
            var i = n("9c6c"), o = n("d53b"), a = n("84f2"), r = n("6821");
            t.exports = n("01f9")(Array, "Array", function (t, e) {
                this._t = r(t), this._i = 0, this._k = e
            }, function () {
                var t = this._t, e = this._k, n = this._i++;
                return !t || n >= t.length ? (this._t = void 0, o(1)) : o(0, "keys" == e ? n : "values" == e ? t[n] : [n, t[n]])
            }, "values"), a.Arguments = a.Array, i("keys"), i("values"), i("entries")
        }, cb7c: function (t, e, n) {
            var i = n("d3f4");
            t.exports = function (t) {
                if (!i(t)) throw TypeError(t + " is not an object!");
                return t
            }
        }, ce10: function (t, e, n) {
            var i = n("69a8"), o = n("6821"), a = n("c366")(!1), r = n("613b")("IE_PROTO");
            t.exports = function (t, e) {
                var n, l = o(t), c = 0, s = [];
                for (n in l) n != r && i(l, n) && s.push(n);
                while (e.length > c) i(l, n = e[c++]) && (~a(s, n) || s.push(n));
                return s
            }
        }, d3f4: function (t, e) {
            t.exports = function (t) {
                return "object" === typeof t ? null !== t : "function" === typeof t
            }
        }, d53b: function (t, e) {
            t.exports = function (t, e) {
                return {value: e, done: !!t}
            }
        }, d8e8: function (t, e) {
            t.exports = function (t) {
                if ("function" != typeof t) throw TypeError(t + " is not a function!");
                return t
            }
        }, e11e: function (t, e) {
            t.exports = "constructor,hasOwnProperty,isPrototypeOf,propertyIsEnumerable,toLocaleString,toString,valueOf".split(",")
        }, f6fd: function (t, e) {
            (function (t) {
                var e = "currentScript", n = t.getElementsByTagName("script");
                e in t || Object.defineProperty(t, e, {
                    get: function () {
                        try {
                            throw new Error
                        } catch (i) {
                            var t, e = (/.*at [^\(]*\((.*):.+:.+\)$/gi.exec(i.stack) || [!1])[1];
                            for (t in n) if (n[t].src == e || "interactive" == n[t].readyState) return n[t];
                            return null
                        }
                    }
                })
            })(document)
        }, fa5b: function (t, e, n) {
            t.exports = n("5537")("native-function-to-string", Function.toString)
        }, fab2: function (t, e, n) {
            var i = n("7726").document;
            t.exports = i && i.documentElement
        }, fb15: function (t, e, n) {
            "use strict";
            var i;
            (n.r(e), "undefined" !== typeof window) && (n("f6fd"), (i = window.document.currentScript) && (i = i.src.match(/(.+\/)[^\/]+\.js(\?.*)?$/)) && (n.p = i[1]));
            n("7f7f"), n("ac6a");
            var o = function () {
                var t = this, e = t.$createElement, n = t._self._c || e;
                return n("div", [n("el-button", {
                    staticClass: "button-new-tag",
                    attrs: {type: "primary", size: "small"},
                    on: {
                        click: function (e) {
                            t.dialogVisible = !0
                        }
                    }
                }, [t._v("选择渠道")]), n("el-dialog", {
                    attrs: {
                        "append-to-body": !0,
                        title: "选择渠道",
                        visible: t.dialogVisible,
                        width: "80%",
                        "before-close": t.handleClose
                    }, on: {
                        "update:visible": function (e) {
                            t.dialogVisible = e
                        }
                    }
                }, [n("div", [n("el-form", {
                    ref: "form",
                    attrs: {model: t.form, "label-width": "160px"}
                }, [n("el-form-item", {
                    attrs: {
                        label: "销售单元:",
                        prop: "saleUnit"
                    }
                }, [t._l(t.clickSaleUnitList, function (e) {
                    return n("el-tag", {
                        key: e.saleUnitCode,
                        attrs: {closable: "", "disable-transitions": !1},
                        on: {
                            close: function (n) {
                                return t.closeSaleUnit(e)
                            }
                        }
                    }, [t._v("\n            " + t._s(e.saleUnitName) + "\n          ")])
                }), n("el-button", {
                    staticClass: "button-new-tag", attrs: {size: "small"}, on: {
                        click: function (e) {
                            t.dialogVisible2 = !0
                        }
                    }
                }, [t._v("+新增销售单元")]), n("el-dialog", {
                    attrs: {
                        "modal-append-to-body": !1,
                        title: "选择销售单元",
                        visible: t.dialogVisible2,
                        width: "30%",
                        "before-close": t.handleClose
                    }, on: {
                        "update:visible": function (e) {
                            t.dialogVisible2 = e
                        }
                    }
                }, [n("div", [n("span", [t._v("销售单元：")]), n("el-select", {
                    attrs: {
                        "value-key": "saleUnitCode",
                        placeholder: "销售单元",
                        filterable: ""
                    }, model: {
                        value: t.clickSaleUnit, callback: function (e) {
                            t.clickSaleUnit = e
                        }, expression: "clickSaleUnit"
                    }
                }, t._l(t.saleUnitList, function (t) {
                    return n("el-option", {key: t.saleUnitCode, attrs: {label: t.saleUnitName, value: t}})
                }), 1)], 1), n("span", {
                    staticClass: "dialog-footer",
                    attrs: {slot: "footer"},
                    slot: "footer"
                }, [n("el-button", {
                    on: {
                        click: function (e) {
                            t.dialogVisible2 = !1, t.clickSaleUnit = null
                        }
                    }
                }, [t._v("取 消")]), n("el-button", {
                    attrs: {type: "primary"}, on: {
                        click: function (e) {
                            t.addSaleUnit(), t.dialogVisible2 = !1
                        }
                    }
                }, [t._v("确 定")])], 1)])], 2), n("el-form-item", {
                    attrs: {
                        label: "渠道类型:",
                        prop: "channelType"
                    }
                }, [t._l(t.clickChannelTypeList, function (e) {
                    return n("el-tag", {
                        key: e.channelType,
                        attrs: {closable: "", "disable-transitions": !1},
                        on: {
                            close: function (n) {
                                return t.closeChannelType(e)
                            }
                        }
                    }, [t._v("\n            " + t._s(e.channelTypeName) + "\n          ")])
                }), n("el-button", {
                    staticClass: "button-new-tag", attrs: {size: "small"}, on: {
                        click: function (e) {
                            t.dialogVisible3 = !0
                        }
                    }
                }, [t._v("+新增渠道类型")]), n("el-dialog", {
                    attrs: {
                        "modal-append-to-body": !1,
                        title: "选择渠道类型",
                        visible: t.dialogVisible3,
                        width: "30%",
                        "before-close": t.handleClose
                    }, on: {
                        "update:visible": function (e) {
                            t.dialogVisible3 = e
                        }
                    }
                }, [n("div", [n("span", [t._v("渠道类型元：")]), n("el-select", {
                    attrs: {
                        "value-key": "channelType",
                        placeholder: "渠道类型",
                        filterable: ""
                    }, model: {
                        value: t.clickChannelType, callback: function (e) {
                            t.clickChannelType = e
                        }, expression: "clickChannelType"
                    }
                }, t._l(t.channelTypeList, function (t) {
                    return n("el-option", {key: t.channelType, attrs: {label: t.channelTypeName, value: t}})
                }), 1)], 1), n("span", {
                    staticClass: "dialog-footer",
                    attrs: {slot: "footer"},
                    slot: "footer"
                }, [n("el-button", {
                    on: {
                        click: function (e) {
                            t.dialogVisible3 = !1, t.clickChannelType = null
                        }
                    }
                }, [t._v("取 消")]), n("el-button", {
                    attrs: {type: "primary"}, on: {
                        click: function (e) {
                            t.addChannelType(), t.dialogVisible3 = !1
                        }
                    }
                }, [t._v("确 定")])], 1)])], 2), n("el-row", {
                    staticStyle: {
                        "text-align": "left",
                        "margin-top": "10px"
                    }
                }, [n("el-button", {
                    attrs: {type: "primary"}, on: {
                        click: function (e) {
                            return t.selectChannel()
                        }
                    }
                }, [t._v("查询")])], 1)], 1), n("el-table", {
                    ref: "table",
                    staticStyle: {width: "100%"},
                    attrs: {data: t.tableData},
                    on: {"selection-change": t.changeFun}
                }, [n("el-table-column", {attrs: {type: "selection"}}), n("el-table-column", {
                    attrs: {
                        prop: "channelCode",
                        label: "渠道code"
                    }
                }), n("el-table-column", {
                    attrs: {
                        prop: "channelName",
                        label: "渠道名称"
                    }
                }), n("el-table-column", {
                    attrs: {
                        prop: "channelTypeName",
                        label: "渠道类型"
                    }
                }), n("el-table-column", {
                    attrs: {
                        prop: "saleUnitName",
                        label: "销售单元"
                    }
                }), n("el-table-column", {
                    attrs: {
                        prop: "parentSaleUnitName",
                        label: "所属单元组"
                    }
                }), n("el-table-column", {
                    attrs: {
                        prop: "orgName",
                        label: "销售组织"
                    }
                })], 1), n("el-pagination", {
                    staticStyle: {float: "right"},
                    attrs: {
                        background: "",
                        layout: "total, sizes, prev, pager, next, jumper",
                        total: t.form.sumNum,
                        "current-page": t.form.pageNum,
                        "page-size": t.form.pageSize,
                        "page-sizes": [10, 20, 50, 100]
                    },
                    on: {"current-change": t.handleCurrentChange, "size-change": t.handleSizeChange}
                })], 1), n("span", {
                    staticClass: "dialog-footer",
                    attrs: {slot: "footer"},
                    slot: "footer"
                }, [n("el-button", {
                    on: {
                        click: function (e) {
                            t.dialogVisible = !1
                        }
                    }
                }, [t._v("取 消")]), n("el-button", {
                    attrs: {type: "primary"}, on: {
                        click: function (e) {
                            t.addChannel(), t.dialogVisible = !1
                        }
                    }
                }, [t._v("确 定")])], 1)])], 1)
            }, a = [], r = {
                name: "basedata-el-channel", data: function () {
                    return {
                        isDev: !0,
                        POOL_NAME: "/basedata-admin",
                        URL: "http://10.0.13.19:8083",
                        form: {pageSize: 10, pageNum: 1, sumNum: 0},
                        dialogVisible: !1,
                        dialogVisible2: !1,
                        dialogVisible3: !1,
                        clickSaleUnit: null,
                        clickChannelType: null,
                        channelTypeList: [],
                        saleUnitList: [],
                        checkSaleUnit: "",
                        checkChannelType: "",
                        clickSaleUnitList: [],
                        clickChannelTypeList: [],
                        tableData: [],
                        multipleSelection: [],
                        multipleSelectionAll: [],
                        idKey: "channelCode",
                        showSelection: !1,
                        isReplace: !1
                    }
                }, created: function () {
                    this.selectChannelTypeList(), this.selectSaleUnitList(), this.selectChannel()
                }, watch: {
                    tableData: function () {
                        this.$nextTick(this.setSelectRow)
                    }
                }, methods: {
                    setSelectRow: function () {
                        if (this.multipleSelectionAll && !(this.multipleSelectionAll.length <= 0)) {
                            var t = this.idKey;
                            this.$refs.table.clearSelection();
                            for (var e = 0; e < this.tableData.length; e++) for (var n = 0; n < this.multipleSelectionAll.length; n++) if (this.tableData[e][t] == this.multipleSelectionAll[n][t]) {
                                this.isReplace && (this.multipleSelectionAll[n] = this.tableData[e]), this.$refs.table.toggleRowSelection(this.tableData[e], !0);
                                break
                            }
                            this.isReplace = !1
                        }
                    }, changePageCoreRecordData: function () {
                        var t = this.idKey, e = this;
                        if (this.multipleSelectionAll = this.multipleSelectionAll || [], this.multipleSelectionAll.length <= 0) this.multipleSelectionAll = this.multipleSelection; else {
                            var n = [];
                            this.multipleSelectionAll.forEach(function (e) {
                                n.push(e[t])
                            });
                            var i = [];
                            this.multipleSelection.forEach(function (o) {
                                i.push(o[t]), n.indexOf(o[t]) < 0 && e.multipleSelectionAll.push(o)
                            });
                            var o = [];
                            this.tableData.forEach(function (e) {
                                i.indexOf(e[t]) < 0 && o.push(e[t])
                            }), o.forEach(function (i) {
                                if (n.indexOf(i) >= 0) for (var o = 0; o < e.multipleSelectionAll.length; o++) if (e.multipleSelectionAll[o][t] == i) {
                                    e.multipleSelectionAll.splice(o, 1);
                                    break
                                }
                            })
                        }
                    }, changeFun: function (t) {
                        this.multipleSelection = t
                    }, handleSizeChange: function (t) {
                        this.changePageCoreRecordData(), this.form.pageSize = t, this.selectChannel()
                    }, handleCurrentChange: function (t) {
                        this.changePageCoreRecordData(), this.form.pageNum = t, this.selectChannel()
                    }, formatType: function (t) {
                        return 1 == t.type ? "单元组" : 2 == t.type ? "单元" : "未知"
                    }, addSaleUnit: function () {
                        var t = this;
                        if (null != this.clickSaleUnit) {
                            var e = !0;
                            this.clickSaleUnitList.forEach(function (n) {
                                if (n.saleUnitCode == t.clickSaleUnit.saleUnitCode) return e = !1, void t.$message({
                                    message: "请勿重复添加",
                                    type: "error"
                                })
                            }), e && this.clickSaleUnitList.push(this.clickSaleUnit), this.clickSaleUnit = null
                        }
                    }, addChannelType: function () {
                        var t = this;
                        if (null != this.clickChannelType) {
                            var e = !0;
                            this.clickChannelTypeList.forEach(function (n) {
                                if (n.channelType == t.clickChannelType.channelType) return e = !1, void t.$message({
                                    message: "请勿重复添加",
                                    type: "error"
                                })
                            }), e && this.clickChannelTypeList.push(this.clickChannelType), this.clickChannelType = null
                        }
                    }, selectChannelTypeList: function () {
                        var t = this,
                            e = 1 == this.isDev ? this.URL + "/v1/channelTypeList" : this.POOL_NAME + "/v1/channelTypeList";
                        this.$http({
                            method: "GET",
                            url: e,
                            data: {},
                            headers: {"Content-Type": "application/json"}
                        }).then(function (e) {
                            t.channelTypeList = e.data
                        })
                    }, selectSaleUnitList: function () {
                        var t = this,
                            e = 1 == this.isDev ? this.URL + "/v1/saleUnitList" : this.POOL_NAME + "/v1/saleUnitList";
                        this.$http({
                            method: "GET",
                            url: e,
                            data: {},
                            headers: {"Content-Type": "application/json"}
                        }).then(function (e) {
                            t.saleUnitList = e.data
                        })
                    }, selectChannel: function () {
                        var t = this, e = this,
                            n = 1 == this.isDev ? this.URL + "/v1/channelPageByList?pageSize=" + e.form.pageSize + "&pageNum=" + e.form.pageNum : this.POOL_NAME + "/v1/channelPageByList?pageSize=" + e.form.pageSize + "&pageNum=" + e.form.pageNum;
                        this.$http({
                            method: "POST",
                            url: n,
                            data: {saleUnitDTOList: e.clickSaleUnitList, channelTypeDTOList: e.clickChannelTypeList},
                            headers: {"Content-Type": "application/json"}
                        }).then(function (n) {
                            n.data.data ? (e.form.sumNum = n.data.data.total, e.form.pageNum = n.data.data.pageNum, e.form.pageSize = n.data.data.pageSize, e.tableData = n.data.data.list) : t.$message({
                                type: "error",
                                message: n.data.msg
                            })
                        })
                    }, handleClose: function (t) {
                        this.$confirm("确认关闭？").then(function (e) {
                            t()
                        }).catch(function (t) {
                        }), this.clickSaleUnit = null, this.clickChannelType = null
                    }, closeSaleUnit: function (t) {
                        this.clickSaleUnitList.splice(this.clickSaleUnitList.indexOf(t), 1)
                    }, closeChannelType: function (t) {
                        this.clickChannelTypeList.splice(this.clickChannelTypeList.indexOf(t), 1)
                    }, closeChannel: function (t) {
                        this.multipleSelectionAll.splice(this.multipleSelectionAll.indexOf(t), 1)
                    }, addChannel: function () {
                        this.changePageCoreRecordData(), this.$emit("change", this.multipleSelectionAll)
                    }
                }
            }, l = r;

            function c(t, e, n, i, o, a, r, l) {
                var c, s = "function" === typeof t ? t.options : t;
                if (e && (s.render = e, s.staticRenderFns = n, s._compiled = !0), i && (s.functional = !0), a && (s._scopeId = "data-v-" + a), r ? (c = function (t) {
                    t = t || this.$vnode && this.$vnode.ssrContext || this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext, t || "undefined" === typeof __VUE_SSR_CONTEXT__ || (t = __VUE_SSR_CONTEXT__), o && o.call(this, t), t && t._registeredComponents && t._registeredComponents.add(r)
                }, s._ssrRegister = c) : o && (c = l ? function () {
                    o.call(this, this.$root.$options.shadowRoot)
                } : o), c) if (s.functional) {
                    s._injectStyles = c;
                    var u = s.render;
                    s.render = function (t, e) {
                        return c.call(e), u(t, e)
                    }
                } else {
                    var f = s.beforeCreate;
                    s.beforeCreate = f ? [].concat(f, c) : [c]
                }
                return {exports: t, options: s}
            }

            var s = c(l, o, a, !1, null, null, null), u = s.exports;
            u.install = function (t) {
                t.component(u.name, u)
            };
            var f = u, p = [f], h = function (t) {
                p.forEach(function (e) {
                    t.component(e.name, e)
                })
            };
            "undefined" !== typeof window && window.Vue && h(window.Vue);
            var d = {install: h, basedata: f};
            e["default"] = d
        }
    })
});
//# sourceMappingURL=basedata-umd-min.js.map