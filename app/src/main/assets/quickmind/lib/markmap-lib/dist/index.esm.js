/*! markmap-lib v0.14.3 | MIT License */
import _extends from '@babel/runtime/helpers/esm/extends';
import { persistCSS, persistJS, Hook, wrapFunction } from 'markmap-common';
import { Remarkable } from 'remarkable';
import remarkableKatex from 'remarkable-katex';
import Prism from 'prismjs';
import loadLanguages from 'prismjs/components/';
import yaml from 'js-yaml';

const template = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n<title>Markmap</title>\n<style>\n* {\n  margin: 0;\n  padding: 0;\n}\n#mindmap {\n  display: block;\n  width: 100vw;\n  height: 100vh;\n}\n</style>\n<!--CSS-->\n</head>\n<body>\n<svg id=\"mindmap\"></svg>\n<!--JS-->\n</body>\n</html>\n";
const BASE_JS = [`https://cdn.jsdelivr.net/npm/d3@${"6.7.0"}`, `https://cdn.jsdelivr.net/npm/markmap-view@${"0.14.3"}`].map(src => ({
  type: 'script',
  data: {
    src
  }
}));
function fillTemplate(root, assets, extra) {
  extra = _extends({
    baseJs: BASE_JS
  }, extra);
  const {
    scripts,
    styles
  } = assets;
  const cssList = [...(styles ? persistCSS(styles) : [])];
  const context = {
    getMarkmap: () => window.markmap,
    getOptions: extra.getOptions,
    jsonOptions: extra.jsonOptions,
    root
  };
  const jsList = [...persistJS([...extra.baseJs, ...(scripts || []), {
    type: 'iife',
    data: {
      fn: (getMarkmap, getOptions, root, jsonOptions) => {
        const markmap = getMarkmap();
        window.mm = markmap.Markmap.create('svg#mindmap', (getOptions || markmap.deriveOptions)(jsonOptions), root);
      },
      getParams: ({
        getMarkmap,
        getOptions,
        root,
        jsonOptions
      }) => {
        return [getMarkmap, getOptions, root, jsonOptions];
      }
    }
  }], context)];
  const html = template.replace('<!--CSS-->', () => cssList.join('')).replace('<!--JS-->', () => jsList.join(''));
  return html;
}

var config$1 = {
  versions: {
    katex: "0.16.0",
    webfontloader: "1.6.28"
  },
  preloadScripts: [{
    type: 'script',
    data: {
      src: `https://cdn.jsdelivr.net/npm/katex@${"0.16.0"}/dist/katex.min.js`
    }
  }],
  scripts: [{
    type: 'iife',
    data: {
      fn: getMarkmap => {
        window.WebFontConfig = {
          custom: {
            families: ['KaTeX_AMS', 'KaTeX_Caligraphic:n4,n7', 'KaTeX_Fraktur:n4,n7', 'KaTeX_Main:n4,n7,i4,i7', 'KaTeX_Math:i4,i7', 'KaTeX_Script', 'KaTeX_SansSerif:n4,n7,i4', 'KaTeX_Size1', 'KaTeX_Size2', 'KaTeX_Size3', 'KaTeX_Size4', 'KaTeX_Typewriter']
          },
          active: () => {
            getMarkmap().refreshHook.call();
          }
        };
      },

      getParams({
        getMarkmap
      }) {
        return [getMarkmap];
      }

    }
  }, {
    type: 'script',
    data: {
      src: `https://cdn.jsdelivr.net/npm/webfontloader@${"1.6.28"}/webfontloader.js`,
      defer: true
    }
  }],
  styles: [{
    type: 'stylesheet',
    data: {
      href: `https://cdn.jsdelivr.net/npm/katex@${"0.16.0"}/dist/katex.min.css`
    }
  }]
};

function createTransformHooks() {
  return {
    parser: new Hook(),
    beforeParse: new Hook(),
    afterParse: new Hook(),
    htmltag: new Hook(),
    retransform: new Hook()
  };
}
/**
 * This function is only used to help type checking.
 */

function definePlugin(plugin) {
  return plugin;
}

const name$2 = 'katex';
var katex = definePlugin({
  name: name$2,
  config: config$1,

  transform(transformHooks) {
    let enableFeature = () => {};

    transformHooks.parser.tap(md => {
      md.use(remarkableKatex);
      md.renderer.rules.katex = wrapFunction(md.renderer.rules.katex, {
        after: () => {
          enableFeature();
        }
      });
    });
    transformHooks.beforeParse.tap((_, context) => {
      enableFeature = () => {
        context.features[name$2] = true;
      };
    });
    return {
      styles: config$1.styles,
      scripts: config$1.scripts
    };
  }

});

var config = {
  versions: {
    prismjs: "1.28.0"
  },
  preloadScripts: [{
    type: 'script',
    data: {
      src: `https://cdn.jsdelivr.net/npm/prismjs@${"1.28.0"}/components/prism-core.min.js`
    }
  }, {
    type: 'script',
    data: {
      src: `https://cdn.jsdelivr.net/npm/prismjs@${"1.28.0"}/plugins/autoloader/prism-autoloader.min.js`
    }
  }],
  styles: [{
    type: 'stylesheet',
    data: {
      href: `https://cdn.jsdelivr.net/npm/prismjs@${"1.28.0"}/themes/prism.css`
    }
  }]
};

const name$1 = 'prism';
var prism = definePlugin({
  name: name$1,
  config,

  transform(transformHooks) {
    let enableFeature = () => {};

    transformHooks.parser.tap(md => {
      md.set({
        highlight: (str, lang) => {
          enableFeature();
          let grammar = Prism.languages[lang];

          if (!grammar && lang) {
            loadLanguages([lang]);
            grammar = Prism.languages[lang];
          }

          if (grammar) {
            return Prism.highlight(str, grammar, lang);
          }

          return '';
        }
      });
    });
    transformHooks.beforeParse.tap((_, context) => {
      enableFeature = () => {
        context.features[name$1] = true;
      };
    });
    return {
      styles: config.styles
    };
  }

});

const name = 'frontmatter';
var frontmatter = definePlugin({
  name,

  transform(transformHooks) {
    transformHooks.beforeParse.tap((md, context) => {
      const origParse = md.parse;
      md.parse = wrapFunction(origParse, {
        before(ctx) {
          const [content] = ctx.args;
          if (!content.startsWith('---\n')) return;
          const endOffset = content.indexOf('\n---\n');
          if (endOffset < 0) return;
          const raw = content.slice(4, endOffset);
          let frontmatter;

          try {
            var _frontmatter;

            frontmatter = yaml.load(raw);

            if ((_frontmatter = frontmatter) != null && _frontmatter.markmap) {
              frontmatter.markmap = normalizeMarkmapJsonOptions(frontmatter.markmap);
            }
          } catch (_unused) {
            return;
          }

          context.frontmatter = frontmatter;
          const offset = endOffset + 5;
          ctx.args[0] = content.slice(offset);
        },

        after() {
          md.parse = origParse;
        }

      });
    });
    return {};
  }

});

function normalizeMarkmapJsonOptions(options) {
  if (!options) return;
  ['color', 'extraJs', 'extraCss'].forEach(key => {
    if (options[key] != null) options[key] = normalizeStringArray(options[key]);
  });
  ['duration', 'maxWidth', 'initialExpandLevel'].forEach(key => {
    if (options[key] != null) options[key] = normalizeNumber(options[key]);
  });
  return options;
}

function normalizeStringArray(value) {
  var _result;

  let result;
  if (typeof value === 'string') result = [value];else if (Array.isArray(value)) result = value.filter(item => item && typeof item === 'string');
  return (_result = result) != null && _result.length ? result : undefined;
}

function normalizeNumber(value) {
  if (isNaN(+value)) return;
  return +value;
}

const plugins = [frontmatter, katex, prism];

function cleanNode(node) {
  if (node.type === 'heading') {
    // drop all paragraphs
    node.children = node.children.filter(item => item.type !== 'paragraph');
  } else if (node.type === 'list_item') {
    var _node$payload;

    // keep first paragraph as content of list_item, drop others
    node.children = node.children.filter(item => {
      if (['paragraph', 'fence'].includes(item.type)) {
        if (!node.content) {
          node.content = item.content;
          node.payload = _extends({}, node.payload, item.payload);
        }

        return false;
      }

      return true;
    });

    if (((_node$payload = node.payload) == null ? void 0 : _node$payload.index) != null) {
      node.content = `${node.payload.index}. ${node.content}`;
    }
  } else if (node.type === 'ordered_list') {
    var _node$payload$startIn, _node$payload2;

    let index = (_node$payload$startIn = (_node$payload2 = node.payload) == null ? void 0 : _node$payload2.startIndex) != null ? _node$payload$startIn : 1;
    node.children.forEach(item => {
      if (item.type === 'list_item') {
        item.payload = _extends({}, item.payload, {
          index
        });
        index += 1;
      }
    });
  }

  if (node.children.length === 0) {
    delete node.children;
  } else {
    node.children.forEach(child => cleanNode(child));

    if (node.children.length === 1 && !node.children[0].content) {
      node.children = node.children[0].children;
    }
  }
}

function resetDepth(node, depth = 0) {
  var _node$children;

  node.depth = depth;
  (_node$children = node.children) == null ? void 0 : _node$children.forEach(child => {
    resetDepth(child, depth + 1);
  });
}

class Transformer {
  constructor(plugins$1 = plugins) {
    this.assetsMap = {};
    this.plugins = plugins$1;
    this.hooks = createTransformHooks();
    const assetsMap = {};

    for (const {
      name,
      transform
    } of plugins$1) {
      assetsMap[name] = transform(this.hooks);
    }

    this.assetsMap = assetsMap;
    const md = new Remarkable('full', {
      html: true,
      breaks: true,
      maxNesting: Infinity
    });
    md.renderer.rules.htmltag = wrapFunction(md.renderer.rules.htmltag, {
      after: ctx => {
        this.hooks.htmltag.call(ctx);
      }
    });
    this.md = md;
    this.hooks.parser.call(md);
  }

  buildTree(tokens) {
    const {
      md
    } = this;
    const root = {
      type: 'root',
      depth: 0,
      content: '',
      children: [],
      payload: {}
    };
    const stack = [root];
    let depth = 0;

    for (const token of tokens) {
      let current = stack[stack.length - 1];

      if (token.type.endsWith('_open')) {
        const type = token.type.slice(0, -5);
        const payload = {};

        if (token.lines) {
          payload.lines = token.lines;
        }

        if (type === 'heading') {
          depth = token.hLevel;

          while (((_current = current) == null ? void 0 : _current.depth) >= depth) {
            var _current;

            stack.pop();
            current = stack[stack.length - 1];
          }
        } else {
          var _current2;

          depth = Math.max(depth, ((_current2 = current) == null ? void 0 : _current2.depth) || 0) + 1;

          if (type === 'ordered_list') {
            payload.startIndex = token.order;
          }
        }

        const item = {
          type,
          depth,
          payload,
          content: '',
          children: []
        };
        current.children.push(item);
        stack.push(item);
      } else if (!current) {
        continue;
      } else if (token.type === `${current.type}_close`) {
        if (current.type === 'heading') {
          depth = current.depth;
        } else {
          stack.pop();
          depth = 0;
        }
      } else if (token.type === 'inline') {
        const revoke = this.hooks.htmltag.tap(ctx => {
          const comment = ctx.result.match(/^<!--([\s\S]*?)-->$/);
          const data = comment == null ? void 0 : comment[1].trim().split(' ');

          if (data[0] === 'fold') {
            current.payload.fold = ['all', 'recursively'].includes(data[1]) ? 2 : 1;
            ctx.result = '';
          }
        });
        const text = md.renderer.render([token], md.options, {});
        revoke();
        current.content = `${current.content || ''}${text}`;
      } else if (token.type === 'fence') {
        let result = md.renderer.render([token], md.options, {}); // Remarkable only adds className to `<code>` but not `<pre>`, copy it to make PrismJS style work.

        const matches = result.match(/<code( class="[^"]*")>/);
        if (matches) result = result.replace('<pre>', `<pre${matches[1]}>`);
        current.children.push({
          type: token.type,
          depth: depth + 1,
          content: result,
          children: []
        });
      } else ;
    }

    return root;
  }

  transform(content) {
    var _root$children;

    const context = {
      features: {}
    };
    this.hooks.beforeParse.call(this.md, context);
    const tokens = this.md.parse(content, {});
    this.hooks.afterParse.call(this.md, context);
    let root = this.buildTree(tokens);
    cleanNode(root);
    if (((_root$children = root.children) == null ? void 0 : _root$children.length) === 1) root = root.children[0];
    resetDepth(root);
    return _extends({}, context, {
      root
    });
  }
  /**
   * Get all assets from enabled plugins or filter them by plugin names as keys.
   */


  getAssets(keys) {
    var _keys;

    const styles = [];
    const scripts = [];
    (_keys = keys) != null ? _keys : keys = this.plugins.map(plugin => plugin.name);

    for (const assets of keys.map(key => this.assetsMap[key])) {
      if (assets) {
        if (assets.styles) styles.push(...assets.styles);
        if (assets.scripts) scripts.push(...assets.scripts);
      }
    }

    return {
      styles,
      scripts
    };
  }
  /**
   * Get used assets by features object returned by `transform`.
   */


  getUsedAssets(features) {
    const keys = this.plugins.map(plugin => plugin.name).filter(name => features[name]);
    return this.getAssets(keys);
  }

}

const transformerVersions = {
  'markmap-lib': '0.14.3',
  d3: "6.7.0"
};

export { Transformer, plugins as builtInPlugins, fillTemplate, transformerVersions };
