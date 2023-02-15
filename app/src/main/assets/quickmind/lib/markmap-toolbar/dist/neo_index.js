/*! markmap-toolbar v0.14.3 | MIT License */ 
! function(t, e) {
    "object" == typeof exports && "undefined" != typeof module ? e(exports) : "function" == typeof define && define.amd ? define(["exports"], e) : e((t = "undefined" != typeof globalThis ? globalThis : t || self).markmap = t.markmap || {})
}(this, (function(t) {
    "use strict";

    function e() {
        return e = Object.assign || function(t) {
            for (var e = 1; e < arguments.length; e++) {
                var r = arguments[e];
                for (var A in r) Object.prototype.hasOwnProperty.call(r, A) && (t[A] = r[A])
            }
            return t
        }, e.apply(this, arguments)
    }
    /*! @gera2ld/jsx-dom v2.1.1 | ISC License */
    var r = "http://www.w3.org/1999/xlink",
        A = {
            show: r,
            actuate: r,
            href: r
        };

    function n(t, e) {
        var r;
        if ("string" == typeof t) r = 1;
        else {
            if ("function" != typeof t) throw new Error("Invalid VNode type");
            r = 2
        }
        return {
            vtype: r,
            type: t,
            props: e
        }
    }
    var i = n;

    function o(t) {
        return t.children
    }
    var a = {
        isSvg: !1
    };

    function s(t, e) {
        if (1 === e.type) null != e.node && t.append(e.node);
        else {
            if (4 !== e.type) throw new Error("Unkown ref type " + JSON.stringify(e));
            e.children.forEach((function(e) {
                s(t, e)
            }))
        }
    }
    var l = {
        className: "class",
        labelFor: "for"
    };

    function h(t, e, r, n) {
        if (e = l[e] || e, !0 === r) t.setAttribute(e, "");
        else if (!1 === r) t.removeAttribute(e);
        else {
            var i = n ? A[e] : void 0;
            void 0 !== i ? t.setAttributeNS(i, e, r) : t.setAttribute(e, r)
        }
    }

    function c(t, e) {
        if (void 0 === e && (e = a), null == t || "boolean" == typeof t) return {
            type: 1,
            node: null
        };
        if (t instanceof Node) return {
            type: 1,
            node: t
        };
        if (2 === (null == (l = t) ? void 0 : l.vtype)) {
            var r = t,
                A = r.type,
                n = r.props;
            if (A === o) {
                var i = document.createDocumentFragment();
                if (n.children) s(i, c(n.children, e));
                return {
                    type: 1,
                    node: i
                }
            }
            return c(A(n), e)
        }
        var l;
        if (function(t) {
                return "string" == typeof t || "number" == typeof t
            }(t)) return {
            type: 1,
            node: document.createTextNode("" + t)
        };
        if (function(t) {
                return 1 === (null == t ? void 0 : t.vtype)
            }(t)) {
            var d, u, v = t,
                f = v.type,
                g = v.props;
            if (e.isSvg || "svg" !== f || (e = Object.assign({}, e, {
                    isSvg: !0
                })), function(t, e, r) {
                    for (var A in e) "key" !== A && "children" !== A && "ref" !== A && ("dangerouslySetInnerHTML" === A ? t.innerHTML = e[A].__html : "innerHTML" === A || "textContent" === A || "innerText" === A ? t[A] = e[A] : A.startsWith("on") ? t[A.toLowerCase()] = e[A] : h(t, A, e[A], r.isSvg))
                }(d = e.isSvg ? document.createElementNS("http://www.w3.org/2000/svg", f) : document.createElement(f), g, e), g.children) {
                var p = e;
                e.isSvg && "foreignObject" === f && (p = Object.assign({}, p, {
                    isSvg: !1
                })), u = c(g.children, p)
            }
            null != u && s(d, u);
            var m = g.ref;
            return "function" == typeof m && m(d), {
                type: 1,
                node: d
            }
        }
        if (Array.isArray(t)) return {
            type: 4,
            children: t.map((function(t) {
                return c(t, e)
            }))
        };
        throw new Error("mount: Invalid Vnode!")
    }

    function d(t) {
        for (var e = [], r = 0; r < t.length; r += 1) {
            var A = t[r];
            Array.isArray(A) ? e = e.concat(d(A)) : null != A && e.push(A)
        }
        return e
    }

    function u(t) {
        return 1 === t.type ? t.node : t.children.map(u)
    }

    function v(t) {
        return Array.isArray(t) ? d(t.map(v)) : u(c(t))
    }
    /*! markmap-common v0.14.2 | MIT License */
    Math.random().toString(36).slice(2, 8);
    const f = "mm-toolbar-item";

    function g({
        title: t,
        content: e,
        onClick: r
    }) {
        return n("div", {
            className: f,
            title: t,
            onClick: r,
            children: e
        })
    }
    let p;
    class m {
        static create(t) {
            const e = new m;
            return e.attach(t), e.render()
        }
        static icon(t, r = {}) {
            return r = e({
                stroke: "none",
                fill: "currentColor",
                "fill-rule": "evenodd"
            }, r), n("svg", {
                width: "36",
                height: "36",
                viewBox: "0 0 20 20",
                children: n("path", e({}, r, {
                    d: t
                }))
            })
        }
        constructor() {
            this.showBrand = !1, this.registry = {}, this.markmap = null, this.register({
                id: "zoomIn",
                title: "缩小",
                content: m.icon("M9 5v4h-4v2h4v4h2v-4h4v-2h-4v-4z"),
                onClick: this.getHandler((t => t.rescale(1.25)))
            }), this.register({
                id: "zoomOut",
                title: "放大",
                content: m.icon("M5 9h10v2h-10z"),
                onClick: this.getHandler((t => t.rescale(.8)))
            }), this.register({
                id: "fit",
                title: "适配屏幕",
                content: m.icon("M4 7h2v-2h2v4h-4zM4 13h2v2h2v-4h-4zM16 7h-2v-2h-2v4h4zM16 13h-2v2h-2v-4h4z"),
                onClick: this.getHandler((t => t.fit()))
            }),
            this.register({
                id: "full",
                title: "全屏屏幕",
                content: m.icon("M4 9v-4h4v2h-2v2zM4 11v4h4v-2h-2v-2zM16 9v-4h-4v2h2v2zM16 11v4h-4v-2h2v-2z"),
                onClick: ()=>{
                    var mouseEv=new MouseEvent('click')
                    $('fullautoclick').dispatchEvent(mouseEv)
                }
            });;
            this.setItems(m.defaultItems)
        }
        setBrand(t) {
            this.showBrand = t
        }
        register(t) {
            this.registry[t.id] = t
        }
        getHandler(t) {
            var e;
            return e = t, t = async (...t) => {
                if (!p) {
                    p = e(...t);
                    try {
                        await p
                    } finally {
                        p = null
                    }
                }
            }, () => {
                this.markmap && t(this.markmap)
            }
        }
        setItems(t) {
            this.items = [...t]
        }
        attach(t) {
            this.markmap = t
        }
        render() {
            const t = this.items.map((t => {
                if ("string" == typeof t) {
                    const e = this.registry[t];
                    return e || console.warn(`[markmap-toolbar] ${t} not found`), e
                }
                return t
            })).filter(Boolean);
            return v(i("div", {
                className: "mm-toolbar",
                children: [t.map(g)]
            }))
        }
    }
    m.defaultItems = ["zoomIn", "zoomOut", "fit","full"], t.Toolbar = m
}));