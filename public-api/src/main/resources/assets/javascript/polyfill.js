/*! Window history polyfill
 * http://purl.eligrey.com/github/classList.js/blob/master/classList.js
 * ie8 / ie9
 */
;
if ("document" in self && !("classList" in document.createElement("_"))) {
	(function(j) {
		"use strict";
		if (!("Element" in j)) {
			return
		}
		var a = "classList",
			f = "prototype",
			m = j.Element[f],
			b = Object,
			k = String[f].trim || function() {
				return this.replace(/^\s+|\s+$/g, "")
			},
			c = Array[f].indexOf || function(q) {
				var p = 0,
					o = this.length;
				for (; p < o; p++) {
					if (p in this && this[p] === q) {
						return p
					}
				}
				return -1
			},
			n = function(o, p) {
				this.name = o;
				this.code = DOMException[o];
				this.message = p
			},
			g = function(p, o) {
				if (o === "") {
					throw new n("SYNTAX_ERR", "An invalid or illegal string was specified")
				}
				if (/\s/.test(o)) {
					throw new n("INVALID_CHARACTER_ERR", "String contains an invalid character")
				}
				return c.call(p, o)
			},
			d = function(s) {
				var r = k.call(s.getAttribute("class") || ""),
					q = r ? r.split(/\s+/) : [],
					p = 0,
					o = q.length;
				for (; p < o; p++) {
					this.push(q[p])
				}
				this._updateClassName = function() {
					s.setAttribute("class", this.toString())
				}
			},
			e = d[f] = [],
			i = function() {
				return new d(this)
			};
		n[f] = Error[f];
		e.item = function(o) {
			return this[o] || null
		};
		e.contains = function(o) {
			o += "";
			return g(this, o) !== -1
		};
		e.add = function() {
			var s = arguments,
				r = 0,
				p = s.length,
				q, o = false;
			do {
				q = s[r] + "";
				if (g(this, q) === -1) {
					this.push(q);
					o = true
				}
			} while (++r < p);
			if (o) {
				this._updateClassName()
			}
		};
		e.remove = function() {
			var t = arguments,
				s = 0,
				p = t.length,
				r, o = false;
			do {
				r = t[s] + "";
				var q = g(this, r);
				if (q !== -1) {
					this.splice(q, 1);
					o = true
				}
			} while (++s < p);
			if (o) {
				this._updateClassName()
			}
		};
		e.toggle = function(p, q) {
			p += "";
			var o = this.contains(p),
				r = o ? q !== true && "remove" : q !== false && "add";
			if (r) {
				this[r](p)
			}
			return !o
		};
		e.toString = function() {
			return this.join(" ")
		};
		if (b.defineProperty) {
			var l = {
				get: i,
				enumerable: true,
				configurable: true
			};
			try {
				b.defineProperty(m, a, l)
			} catch (h) {
				if (h.number === -2146823252) {
					l.enumerable = false;
					b.defineProperty(m, a, l)
				}
			}
		} else {
			if (b[f].__defineGetter__) {
				m.__defineGetter__(a, i)
			}
		}
	}(self))
};;


/* IE8 Add event listener support
 *  https://developer.mozilla.org/en/docs/Web/API/EventTarget.addEventListener
 */
(function() {
	if (!Event.prototype.preventDefault) {
		Event.prototype.preventDefault = function() {
			this.returnValue = false
		}
	}
	if (!Event.prototype.stopPropagation) {
		Event.prototype.stopPropagation = function() {
			this.cancelBubble = true
		}
	}
	if (!Element.prototype.addEventListener) {
		var e = [];
		var t = function(t, n) {
			var r = this;
			var i = function(e) {
				e.target = e.srcElement;
				e.currentTarget = r;
				if (n.handleEvent) {
					n.handleEvent(e)
				} else {
					n.call(r, e)
				}
			};
			if (t == "DOMContentLoaded") {
				var s = function(e) {
					if (document.readyState == "complete") {
						i(e)
					}
				};
				document.attachEvent("onreadystatechange", s);
				e.push({
					object: this,
					type: t,
					listener: n,
					wrapper: s
				});
				if (document.readyState == "complete") {
					var o = new Event;
					o.srcElement = window;
					s(o)
				}
			} else {
				this.attachEvent("on" + t, i);
				e.push({
					object: this,
					type: t,
					listener: n,
					wrapper: i
				})
			}
		};
		var n = function(t, n) {
			var r = 0;
			while (r < e.length) {
				var i = e[r];
				if (i.object == this && i.type == t && i.listener == n) {
					if (t == "DOMContentLoaded") {
						this.detachEvent("onreadystatechange", i.wrapper)
					} else {
						this.detachEvent("on" + t, i.wrapper)
					}
					break
				}++r
			}
		};
		Element.prototype.addEventListener = t;
		Element.prototype.removeEventListener = n;
		if (HTMLDocument) {
			HTMLDocument.prototype.addEventListener = t;
			HTMLDocument.prototype.removeEventListener = n
		}
		if (Window) {
			Window.prototype.addEventListener = t;
			Window.prototype.removeEventListener = n
		}
	}
})();


/*! Window history polyfill
 * https://github.com/balupton/history.js
 * ie8 / ie9
 */
(function(a, b) {
	"use strict";
	var c = a.console || b,
		d = a.document,
		e = a.navigator,
		f = a.sessionStorage || !1,
		g = a.setTimeout,
		h = a.clearTimeout,
		i = a.setInterval,
		j = a.clearInterval,
		k = a.JSON,
		l = a.alert,
		m = a.History = a.History || {},
		n = a.history;
	k.stringify = k.stringify || k.encode, k.parse = k.parse || k.decode;
	if (typeof m.init != "undefined") throw new Error("History.js Core has already been loaded...");
	m.init = function() {
		return typeof m.Adapter == "undefined" ? !1 : (typeof m.initCore != "undefined" && m.initCore(), typeof m.initHtml4 != "undefined" && m.initHtml4(), !0)
	}, m.initCore = function() {
		if (typeof m.initCore.initialized != "undefined") return !1;
		m.initCore.initialized = !0, m.options = m.options || {}, m.options.hashChangeInterval = m.options.hashChangeInterval || 100, m.options.safariPollInterval = m.options.safariPollInterval || 500, m.options.doubleCheckInterval = m.options.doubleCheckInterval || 500, m.options.storeInterval = m.options.storeInterval || 1e3, m.options.busyDelay = m.options.busyDelay || 250, m.options.debug = m.options.debug || !1, m.options.initialTitle = m.options.initialTitle || d.title, m.intervalList = [], m.clearAllIntervals = function() {
			var a, b = m.intervalList;
			if (typeof b != "undefined" && b !== null) {
				for (a = 0; a < b.length; a++) j(b[a]);
				m.intervalList = null
			}
		}, m.debug = function() {
			(m.options.debug || !1) && m.log.apply(m, arguments)
		}, m.log = function() {
			var a = typeof c != "undefined" && typeof c.log != "undefined" && typeof c.log.apply != "undefined",
				b = d.getElementById("log"),
				e, f, g, h, i;
			a ? (h = Array.prototype.slice.call(arguments), e = h.shift(), typeof c.debug != "undefined" ? c.debug.apply(c, [e, h]) : c.log.apply(c, [e, h])) : e = "\n" + arguments[0] + "\n";
			for (f = 1, g = arguments.length; f < g; ++f) {
				i = arguments[f];
				if (typeof i == "object" && typeof k != "undefined") try {
					i = k.stringify(i)
				} catch (j) {}
				e += "\n" + i + "\n"
			}
			return b ? (b.value += e + "\n-----\n", b.scrollTop = b.scrollHeight - b.clientHeight) : a || l(e), !0
		}, m.getInternetExplorerMajorVersion = function() {
			var a = m.getInternetExplorerMajorVersion.cached = typeof m.getInternetExplorerMajorVersion.cached != "undefined" ? m.getInternetExplorerMajorVersion.cached : function() {
				var a = 3,
					b = d.createElement("div"),
					c = b.getElementsByTagName("i");
				while ((b.innerHTML = "<!--[if gt IE " + ++a + "]><i></i><![endif]-->") && c[0]);
				return a > 4 ? a : !1
			}();
			return a
		}, m.isInternetExplorer = function() {
			var a = m.isInternetExplorer.cached = typeof m.isInternetExplorer.cached != "undefined" ? m.isInternetExplorer.cached : Boolean(m.getInternetExplorerMajorVersion());
			return a
		}, m.emulated = {
			pushState: !Boolean(a.history && a.history.pushState && a.history.replaceState && !/ Mobile\/([1-7][a-z]|(8([abcde]|f(1[0-8]))))/i.test(e.userAgent) && !/AppleWebKit\/5([0-2]|3[0-2])/i.test(e.userAgent)),
			hashChange: Boolean(!("onhashchange" in a || "onhashchange" in d) || m.isInternetExplorer() && m.getInternetExplorerMajorVersion() < 8)
		}, m.enabled = !m.emulated.pushState, m.bugs = {
			setHash: Boolean(!m.emulated.pushState && e.vendor === "Apple Computer, Inc." && /AppleWebKit\/5([0-2]|3[0-3])/.test(e.userAgent)),
			safariPoll: Boolean(!m.emulated.pushState && e.vendor === "Apple Computer, Inc." && /AppleWebKit\/5([0-2]|3[0-3])/.test(e.userAgent)),
			ieDoubleCheck: Boolean(m.isInternetExplorer() && m.getInternetExplorerMajorVersion() < 8),
			hashEscape: Boolean(m.isInternetExplorer() && m.getInternetExplorerMajorVersion() < 7)
		}, m.isEmptyObject = function(a) {
			for (var b in a) return !1;
			return !0
		}, m.cloneObject = function(a) {
			var b, c;
			return a ? (b = k.stringify(a), c = k.parse(b)) : c = {}, c
		}, m.getRootUrl = function() {
			var a = d.location.protocol + "//" + (d.location.hostname || d.location.host);
			if (d.location.port || !1) a += ":" + d.location.port;
			return a += "/", a
		}, m.getBaseHref = function() {
			var a = d.getElementsByTagName("base"),
				b = null,
				c = "";
			return a.length === 1 && (b = a[0], c = b.href.replace(/[^\/]+$/, "")), c = c.replace(/\/+$/, ""), c && (c += "/"), c
		}, m.getBaseUrl = function() {
			var a = m.getBaseHref() || m.getBasePageUrl() || m.getRootUrl();
			return a
		}, m.getPageUrl = function() {
			var a = m.getState(!1, !1),
				b = (a || {}).url || d.location.href,
				c;
			return c = b.replace(/\/+$/, "").replace(/[^\/]+$/, function(a, b, c) {
				return /\./.test(a) ? a : a + "/"
			}), c
		}, m.getBasePageUrl = function() {
			var a = d.location.href.replace(/[#\?].*/, "").replace(/[^\/]+$/, function(a, b, c) {
				return /[^\/]$/.test(a) ? "" : a
			}).replace(/\/+$/, "") + "/";
			return a
		}, m.getFullUrl = function(a, b) {
			var c = a,
				d = a.substring(0, 1);
			return b = typeof b == "undefined" ? !0 : b, /[a-z]+\:\/\//.test(a) || (d === "/" ? c = m.getRootUrl() + a.replace(/^\/+/, "") : d === "#" ? c = m.getPageUrl().replace(/#.*/, "") + a : d === "?" ? c = m.getPageUrl().replace(/[\?#].*/, "") + a : b ? c = m.getBaseUrl() + a.replace(/^(\.\/)+/, "") : c = m.getBasePageUrl() + a.replace(/^(\.\/)+/, "")), c.replace(/\#$/, "")
		}, m.getShortUrl = function(a) {
			var b = a,
				c = m.getBaseUrl(),
				d = m.getRootUrl();
			return m.emulated.pushState && (b = b.replace(c, "")), b = b.replace(d, "/"), m.isTraditionalAnchor(b) && (b = "./" + b), b = b.replace(/^(\.\/)+/g, "./").replace(/\#$/, ""), b
		}, m.store = {}, m.idToState = m.idToState || {}, m.stateToId = m.stateToId || {}, m.urlToId = m.urlToId || {}, m.storedStates = m.storedStates || [], m.savedStates = m.savedStates || [], m.normalizeStore = function() {
			m.store.idToState = m.store.idToState || {}, m.store.urlToId = m.store.urlToId || {}, m.store.stateToId = m.store.stateToId || {}
		}, m.getState = function(a, b) {
			typeof a == "undefined" && (a = !0), typeof b == "undefined" && (b = !0);
			var c = m.getLastSavedState();
			return !c && b && (c = m.createStateObject()), a && (c = m.cloneObject(c), c.url = c.cleanUrl || c.url), c
		}, m.getIdByState = function(a) {
			var b = m.extractId(a.url),
				c;
			if (!b) {
				c = m.getStateString(a);
				if (typeof m.stateToId[c] != "undefined") b = m.stateToId[c];
				else if (typeof m.store.stateToId[c] != "undefined") b = m.store.stateToId[c];
				else {
					for (;;) {
						b = (new Date).getTime() + String(Math.random()).replace(/\D/g, "");
						if (typeof m.idToState[b] == "undefined" && typeof m.store.idToState[b] == "undefined") break
					}
					m.stateToId[c] = b, m.idToState[b] = a
				}
			}
			return b
		}, m.normalizeState = function(a) {
			var b, c;
			if (!a || typeof a != "object") a = {};
			if (typeof a.normalized != "undefined") return a;
			if (!a.data || typeof a.data != "object") a.data = {};
			b = {}, b.normalized = !0, b.title = a.title || "", b.url = m.getFullUrl(m.unescapeString(a.url || d.location.href)), b.hash = m.getShortUrl(b.url), b.data = m.cloneObject(a.data), b.id = m.getIdByState(b), b.cleanUrl = b.url.replace(/\??\&_suid.*/, ""), b.url = b.cleanUrl, c = !m.isEmptyObject(b.data);
			if (b.title || c) b.hash = m.getShortUrl(b.url).replace(/\??\&_suid.*/, ""), /\?/.test(b.hash) || (b.hash += "?"), b.hash += "&_suid=" + b.id;
			return b.hashedUrl = m.getFullUrl(b.hash), (m.emulated.pushState || m.bugs.safariPoll) && m.hasUrlDuplicate(b) && (b.url = b.hashedUrl), b
		}, m.createStateObject = function(a, b, c) {
			var d = {
				data: a,
				title: b,
				url: c
			};
			return d = m.normalizeState(d), d
		}, m.getStateById = function(a) {
			a = String(a);
			var c = m.idToState[a] || m.store.idToState[a] || b;
			return c
		}, m.getStateString = function(a) {
			var b, c, d;
			return b = m.normalizeState(a), c = {
				data: b.data,
				title: a.title,
				url: a.url
			}, d = k.stringify(c), d
		}, m.getStateId = function(a) {
			var b, c;
			return b = m.normalizeState(a), c = b.id, c
		}, m.getHashByState = function(a) {
			var b, c;
			return b = m.normalizeState(a), c = b.hash, c
		}, m.extractId = function(a) {
			var b, c, d;
			return c = /(.*)\&_suid=([0-9]+)$/.exec(a), d = c ? c[1] || a : a, b = c ? String(c[2] || "") : "", b || !1
		}, m.isTraditionalAnchor = function(a) {
			var b = !/[\/\?\.]/.test(a);
			return b
		}, m.extractState = function(a, b) {
			var c = null,
				d, e;
			return b = b || !1, d = m.extractId(a), d && (c = m.getStateById(d)), c || (e = m.getFullUrl(a), d = m.getIdByUrl(e) || !1, d && (c = m.getStateById(d)), !c && b && !m.isTraditionalAnchor(a) && (c = m.createStateObject(null, null, e))), c
		}, m.getIdByUrl = function(a) {
			var c = m.urlToId[a] || m.store.urlToId[a] || b;
			return c
		}, m.getLastSavedState = function() {
			return m.savedStates[m.savedStates.length - 1] || b
		}, m.getLastStoredState = function() {
			return m.storedStates[m.storedStates.length - 1] || b
		}, m.hasUrlDuplicate = function(a) {
			var b = !1,
				c;
			return c = m.extractState(a.url), b = c && c.id !== a.id, b
		}, m.storeState = function(a) {
			return m.urlToId[a.url] = a.id, m.storedStates.push(m.cloneObject(a)), a
		}, m.isLastSavedState = function(a) {
			var b = !1,
				c, d, e;
			return m.savedStates.length && (c = a.id, d = m.getLastSavedState(), e = d.id, b = c === e), b
		}, m.saveState = function(a) {
			return m.isLastSavedState(a) ? !1 : (m.savedStates.push(m.cloneObject(a)), !0)
		}, m.getStateByIndex = function(a) {
			var b = null;
			return typeof a == "undefined" ? b = m.savedStates[m.savedStates.length - 1] : a < 0 ? b = m.savedStates[m.savedStates.length + a] : b = m.savedStates[a], b
		}, m.getHash = function() {
			var a = m.unescapeHash(d.location.hash);
			return a
		}, m.unescapeString = function(b) {
			var c = b,
				d;
			for (;;) {
				d = a.unescape(c);
				if (d === c) break;
				c = d
			}
			return c
		}, m.unescapeHash = function(a) {
			var b = m.normalizeHash(a);
			return b = m.unescapeString(b), b
		}, m.normalizeHash = function(a) {
			var b = a.replace(/[^#]*#/, "").replace(/#.*/, "");
			return b
		}, m.setHash = function(a, b) {
			var c, e, f;
			return b !== !1 && m.busy() ? (m.pushQueue({
				scope: m,
				callback: m.setHash,
				args: arguments,
				queue: b
			}), !1) : (c = m.escapeHash(a), m.busy(!0), e = m.extractState(a, !0), e && !m.emulated.pushState ? m.pushState(e.data, e.title, e.url, !1) : d.location.hash !== c && (m.bugs.setHash ? (f = m.getPageUrl(), m.pushState(null, null, f + "#" + c, !1)) : d.location.hash = c), m)
		}, m.escapeHash = function(b) {
			var c = m.normalizeHash(b);
			return c = a.escape(c), m.bugs.hashEscape || (c = c.replace(/\%21/g, "!").replace(/\%26/g, "&").replace(/\%3D/g, "=").replace(/\%3F/g, "?")), c
		}, m.getHashByUrl = function(a) {
			var b = String(a).replace(/([^#]*)#?([^#]*)#?(.*)/, "$2");
			return b = m.unescapeHash(b), b
		}, m.setTitle = function(a) {
			var b = a.title,
				c;
			b || (c = m.getStateByIndex(0), c && c.url === a.url && (b = c.title || m.options.initialTitle));
			try {
				d.getElementsByTagName("title")[0].innerHTML = b.replace("<", "&lt;").replace(">", "&gt;").replace(" & ", " &amp; ")
			} catch (e) {}
			return d.title = b, m
		}, m.queues = [], m.busy = function(a) {
			typeof a != "undefined" ? m.busy.flag = a : typeof m.busy.flag == "undefined" && (m.busy.flag = !1);
			if (!m.busy.flag) {
				h(m.busy.timeout);
				var b = function() {
					var a, c, d;
					if (m.busy.flag) return;
					for (a = m.queues.length - 1; a >= 0; --a) {
						c = m.queues[a];
						if (c.length === 0) continue;
						d = c.shift(), m.fireQueueItem(d), m.busy.timeout = g(b, m.options.busyDelay)
					}
				};
				m.busy.timeout = g(b, m.options.busyDelay)
			}
			return m.busy.flag
		}, m.busy.flag = !1, m.fireQueueItem = function(a) {
			return a.callback.apply(a.scope || m, a.args || [])
		}, m.pushQueue = function(a) {
			return m.queues[a.queue || 0] = m.queues[a.queue || 0] || [], m.queues[a.queue || 0].push(a), m
		}, m.queue = function(a, b) {
			return typeof a == "function" && (a = {
				callback: a
			}), typeof b != "undefined" && (a.queue = b), m.busy() ? m.pushQueue(a) : m.fireQueueItem(a), m
		}, m.clearQueue = function() {
			return m.busy.flag = !1, m.queues = [], m
		}, m.stateChanged = !1, m.doubleChecker = !1, m.doubleCheckComplete = function() {
			return m.stateChanged = !0, m.doubleCheckClear(), m
		}, m.doubleCheckClear = function() {
			return m.doubleChecker && (h(m.doubleChecker), m.doubleChecker = !1), m
		}, m.doubleCheck = function(a) {
			return m.stateChanged = !1, m.doubleCheckClear(), m.bugs.ieDoubleCheck && (m.doubleChecker = g(function() {
				return m.doubleCheckClear(), m.stateChanged || a(), !0
			}, m.options.doubleCheckInterval)), m
		}, m.safariStatePoll = function() {
			var b = m.extractState(d.location.href),
				c;
			if (!m.isLastSavedState(b)) c = b;
			else return;
			return c || (c = m.createStateObject()), m.Adapter.trigger(a, "popstate"), m
		}, m.back = function(a) {
			return a !== !1 && m.busy() ? (m.pushQueue({
				scope: m,
				callback: m.back,
				args: arguments,
				queue: a
			}), !1) : (m.busy(!0), m.doubleCheck(function() {
				m.back(!1)
			}), n.go(-1), !0)
		}, m.forward = function(a) {
			return a !== !1 && m.busy() ? (m.pushQueue({
				scope: m,
				callback: m.forward,
				args: arguments,
				queue: a
			}), !1) : (m.busy(!0), m.doubleCheck(function() {
				m.forward(!1)
			}), n.go(1), !0)
		}, m.go = function(a, b) {
			var c;
			if (a > 0)
				for (c = 1; c <= a; ++c) m.forward(b);
			else {
				if (!(a < 0)) throw new Error("History.go: History.go requires a positive or negative integer passed.");
				for (c = -1; c >= a; --c) m.back(b)
			}
			return m
		};
		if (m.emulated.pushState) {
			var o = function() {};
			m.pushState = m.pushState || o, m.replaceState = m.replaceState || o
		} else m.onPopState = function(b, c) {
			var e = !1,
				f = !1,
				g, h;
			return m.doubleCheckComplete(), g = m.getHash(), g ? (h = m.extractState(g || d.location.href, !0), h ? m.replaceState(h.data, h.title, h.url, !1) : (m.Adapter.trigger(a, "anchorchange"), m.busy(!1)), m.expectedStateId = !1, !1) : (e = m.Adapter.extractEventData("state", b, c) || !1, e ? f = m.getStateById(e) : m.expectedStateId ? f = m.getStateById(m.expectedStateId) : f = m.extractState(d.location.href), f || (f = m.createStateObject(null, null, d.location.href)), m.expectedStateId = !1, m.isLastSavedState(f) ? (m.busy(!1), !1) : (m.storeState(f), m.saveState(f), m.setTitle(f), m.Adapter.trigger(a, "statechange"), m.busy(!1), !0))
		}, m.Adapter.bind(a, "popstate", m.onPopState), m.pushState = function(b, c, d, e) {
			if (m.getHashByUrl(d) && m.emulated.pushState) throw new Error("History.js does not support states with fragement-identifiers (hashes/anchors).");
			if (e !== !1 && m.busy()) return m.pushQueue({
				scope: m,
				callback: m.pushState,
				args: arguments,
				queue: e
			}), !1;
			m.busy(!0);
			var f = m.createStateObject(b, c, d);
			return m.isLastSavedState(f) ? m.busy(!1) : (m.storeState(f), m.expectedStateId = f.id, n.pushState(f.id, f.title, f.url), m.Adapter.trigger(a, "popstate")), !0
		}, m.replaceState = function(b, c, d, e) {
			if (m.getHashByUrl(d) && m.emulated.pushState) throw new Error("History.js does not support states with fragement-identifiers (hashes/anchors).");
			if (e !== !1 && m.busy()) return m.pushQueue({
				scope: m,
				callback: m.replaceState,
				args: arguments,
				queue: e
			}), !1;
			m.busy(!0);
			var f = m.createStateObject(b, c, d);
			return m.isLastSavedState(f) ? m.busy(!1) : (m.storeState(f), m.expectedStateId = f.id, n.replaceState(f.id, f.title, f.url), m.Adapter.trigger(a, "popstate")), !0
		};
		if (f) {
			try {
				m.store = k.parse(f.getItem("History.store")) || {}
			} catch (p) {
				m.store = {}
			}
			m.normalizeStore()
		} else m.store = {}, m.normalizeStore();
		m.Adapter.bind(a, "beforeunload", m.clearAllIntervals), m.Adapter.bind(a, "unload", m.clearAllIntervals), m.saveState(m.storeState(m.extractState(d.location.href, !0))), f && (m.onUnload = function() {
			var a, b;
			try {
				a = k.parse(f.getItem("History.store")) || {}
			} catch (c) {
				a = {}
			}
			a.idToState = a.idToState || {}, a.urlToId = a.urlToId || {}, a.stateToId = a.stateToId || {};
			for (b in m.idToState) {
				if (!m.idToState.hasOwnProperty(b)) continue;
				a.idToState[b] = m.idToState[b]
			}
			for (b in m.urlToId) {
				if (!m.urlToId.hasOwnProperty(b)) continue;
				a.urlToId[b] = m.urlToId[b]
			}
			for (b in m.stateToId) {
				if (!m.stateToId.hasOwnProperty(b)) continue;
				a.stateToId[b] = m.stateToId[b]
			}
			m.store = a, m.normalizeStore(), f.setItem("History.store", k.stringify(a))
		}, m.intervalList.push(i(m.onUnload, m.options.storeInterval)), m.Adapter.bind(a, "beforeunload", m.onUnload), m.Adapter.bind(a, "unload", m.onUnload));
		if (!m.emulated.pushState) {
			m.bugs.safariPoll && m.intervalList.push(i(m.safariStatePoll, m.options.safariPollInterval));
			if (e.vendor === "Apple Computer, Inc." || (e.appCodeName || "") === "Mozilla") m.Adapter.bind(a, "hashchange", function() {
				m.Adapter.trigger(a, "popstate")
			}), m.getHash() && m.Adapter.onDomLoad(function() {
				m.Adapter.trigger(a, "hashchange")
			})
		}
	}, m.init()
})(window);


/*! Template polyfill
 * http://jsfiddle.net/brianblakely/h3EmY/
 * ie8 / ie9
 */
(function(t) {
	if ("content" in t.createElement("template")) {
		return false
	}
	var n = t.getElementsByTagName("template"),
		r = n.length,
		i, s, o, u;
	for (var a = 0; a < r; ++a) {
		i = n[a];
		s = i.childNodes;
		o = s.length;
		u = t.createDocumentFragment();
		while (s[0]) {
			u.appendChild(s[0])
		}
		i.content = u
	}
})(document)
