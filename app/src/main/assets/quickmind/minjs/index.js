"use strict";

function _typeof(obj) { "@babel/helpers - typeof"; return _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (obj) { return typeof obj; } : function (obj) { return obj && "function" == typeof Symbol && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }, _typeof(obj); }
function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, _toPropertyKey(descriptor.key), descriptor); } }
function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); Object.defineProperty(Constructor, "prototype", { writable: false }); return Constructor; }
function _toPropertyKey(arg) { var key = _toPrimitive(arg, "string"); return _typeof(key) === "symbol" ? key : String(key); }
function _toPrimitive(input, hint) { if (_typeof(input) !== "object" || input === null) return input; var prim = input[Symbol.toPrimitive]; if (prim !== undefined) { var res = prim.call(input, hint || "default"); if (_typeof(res) !== "object") return res; throw new TypeError("@@toPrimitive must return a primitive value."); } return (hint === "string" ? String : Number)(input); }
//静态资源
var waitloading_id = "waitloading";
var offcanvasLabel_id = "offcanvasLabel";
var toolbar_tile = "toolbar_tile";
var offcanvasMain_id = "offcanvasMain";
var filetype_img_path = "../basic/img/filetype-md.svg";

/**配置文件操作函数*/
var Anco = /*#__PURE__*/function () {
  /**
   * 
   * @param {String} configStr 
   */
  function Anco(configStr) {
    _classCallCheck(this, Anco);
    var configObj = {
      loadState: 'none',
      tile: '（未打开文件）',
      list: []
    };
    if (typeof configStr == 'string' && configStr.length > 0) {
      var obj;
      try {
        obj = JSON.parse(configStr);
      } catch (error) {
        obj = {
          loadState: 'none',
          tile: '（未打开文件）',
          list: []
        };
        console.log(error);
      }
      configObj = obj;
    }
    var _configObj = configObj,
      _configObj$loadState = _configObj.loadState,
      loadState = _configObj$loadState === void 0 ? 'none' : _configObj$loadState,
      _configObj$tile = _configObj.tile,
      tile = _configObj$tile === void 0 ? '（未打开文件）' : _configObj$tile,
      _configObj$list = _configObj.list,
      list = _configObj$list === void 0 ? [] : _configObj$list;
    this.tile = tile;
    var filteredList = list.filter(function (iet) {
      return iet.endsWith('.md');
    });
    this.list = filteredList;
    this.loadState = loadState;
  }

  //移除元素
  /**
   * 
   * @param {String} ite 
   */
  _createClass(Anco, [{
    key: "rem",
    value: function rem(ite) {
      if (this.list.includes(ite)) {
        this.list = this.list.filter(function (ie) {
          return ie != ite;
        });
        console.log(this.list);
        if (this.tile == ite && this.loadState == 'exist') {
          this.close();
          loadPage(this);
        }
      }
    }

    //添加元素
    /**
     * 
     * @param {String} ite 
     */
  }, {
    key: "add",
    value: function add(ite) {
      this.list.push(ite);
      //创建新文件
      anPutData(ite, '');
    }

    //复制文件
    /**
     * 
     * @param {String} ite 
     * @param {String} contents 
     */
  }, {
    key: "copy",
    value: function copy(ite, contents) {
      this.list.push(ite);
      //创建新文件
      var __contents = contents || "";
      anPutData(ite, __contents);
    }

    //改变标题
    /**
     * 
     * @param {String} ite 
     */
  }, {
    key: "rename",
    value: function rename(old_ite, ite) {
      this.tile = ite;
      var index = this.list.indexOf(old_ite);
      this.list[index] = ite;
      var old_file_contents = java.direader(old_ite, 'mind');
      anPutData(ite, old_file_contents);
      console.log(this.list);
    }

    //关闭文件
  }, {
    key: "close",
    value: function close() {
      this.loadState = 'none';
      this.tile = '（未打开文件）';
    }

    //打开文件
    /**
     * 
     * @param {String} ite 
     */
  }, {
    key: "open",
    value: function open(ite) {
      this.loadState = 'exist';
      this.tile = ite;
    }

    //转换JSON
  }, {
    key: "toJson",
    value: function toJson() {
      var jsons = {
        loadState: this.loadState,
        tile: this.tile,
        list: this.list
      };
      console.log(jsons);
      return JSON.stringify(jsons);
    }
  }]);
  return Anco;
}();
/**配置文件操作函数*/
//"{\"loadState\":\"exist\",\"tile\":\"默认.md\",\"list\":[\"默认.md\"]}"
var Anconfigs = java.dipasser('getConfig', 'mind'); //获取配置参数

var AnconfigObj = new Anco(Anconfigs);
console.log(AnconfigObj);

/*侧边栏组件*/
var SideBar = {
  setHeader: function setHeader(title) {
    $(offcanvasLabel_id).innerHTML = title;
    $(toolbar_tile).innerHTML = title;
  },
  getHeader: function getHeader() {
    return $(offcanvasLabel_id).innerHTML;
  },
  setHexport: function setHexport(til) {
    return $(toolbar_tile).attr('hexport', til);
  },
  getHexport: function getHexport() {
    return $(toolbar_tile).getAttribute('hexport');
  },
  setMain: function setMain(source) {
    return $(offcanvasMain_id).innerHTML = source;
  }
};
/*侧边栏组件*/

/*----侧边栏文件列表组件-----*/

/**
 * 
 * @param {HTMLObjectElement} ele 
 */
var openMdFile = function openMdFile(ele) {
  AnconfigObj.open(ele);
  if (AnconfigObj.loadState == 'exist') {
    SideBar.setHeader(ele);
    LoadProcessor(AnconfigObj);
  } else {
    loadPage(AnconfigObj);
  }
  anPutConfig('.configs', AnconfigObj.toJson());
};

/**
 * 
 * @param {String} id 
 */
var delDoc = function delDoc(id) {
  var root_ele = $(id);
  var tie_id = id.replace('root_', '');
  var tie = $(tie_id).getAttribute('data-bs-name');
  var offcanvasMain = $(offcanvasMain_id);
  offcanvasMain.del(root_ele);
  if (offcanvasMain.children.length == 0 || offcanvasMain.children === undefined) {
    offcanvasMain.innerHTML = '<h5 class="mt-5 text-center">暂无文件</h5>';
  }
  AnconfigObj.rem(tie);
  anPutConfig('.configs', AnconfigObj.toJson());
};

/**
 * 
 * @param {String} id 
 */
var editDoc = function editDoc(id) {
  var doc_ele = $(id);
  var old_name = doc_ele.getAttribute('data-bs-name');
  var vl = $('editFn_input_file').value;
  var neo_name = vl + '.md';
  var checkIsDuplicate = function checkIsDuplicate(old, neo) {
    if (old == neo) return false;
    var isSame = AnconfigObj.list.filter(function (it) {
      return it == neo;
    });
    return isSame.length > 0;
  };
  if (vl.length > 0) {
    if (!checkIsDuplicate(old_name, neo_name)) {
      if (SideBar.getHeader() == old_name) SideBar.setHeader(neo_name);
      doc_ele.setAttribute('data-bs-name', neo_name);
      doc_ele.setAttribute('onclick', "openMdFile('" + neo_name + "')");
      doc_ele.innerHTML = neo_name;
      AnconfigObj.rename(old_name, neo_name);

      // 触发对象可以是任何元素或其他事件目标
      $('editFn_input_click').autoclick();
    } else {
      var hintObj = $('editFn_input_hint');
      hintObj.innerHTML = '文件名重复了，请重新输入！';
      hintObj.setAttribute('class', 'text-danger ps-2');
      setTimeout(function () {
        return hintObj.setAttribute('class', 'visually-hidden');
      }, 1000);
    }
  } else {
    var hintObj = $('editFn_input_hint');
    hintObj.innerHTML = '文件名不能为空！';
    hintObj.setAttribute('class', 'text-danger ps-2');
    setTimeout(function () {
      return hintObj.setAttribute('class', 'visually-hidden');
    }, 1000);
  }
  anPutConfig('.configs', AnconfigObj.toJson());
};
var docItem = "\n<div class=\"item\" id=\"root_{more_id}\">\n    <div class=\"item-box\">\n        <div class=\"column\" style=\"flex:9;width:100%\">\n            <div class=\"row g-0\">\n                <div class=\"col-2 bg-white\">\n                    <img src=\"".concat(filetype_img_path, "\" class=\"img-fluid\" style=\"opacity:.76\" width=\"32\" height=\"32\" alt=\"...\"></img>\n                </div>\n                <div class=\"col-10 docClick ps-2\" id=\"{more_id}\" onclick=\"openMdFile('{doc}')\" data-bs-name=\"{doc}\" data-bs-dismiss=\"offcanvas\">{doc}</div>\n            </div>\n        \n        </div>\n        <div class=\"column\" style=\"flex: 3;\" data-bs-toggle=\"modal\" data-bs-target=\"#confirmDialogue\" data-bs-whatever=\"editFn\" data-bs-id=\"{more_id}\">\u7F16\u8F91</div>\n    </div>\n    <div class=\"item-del\" data-bs-toggle=\"modal\" data-bs-target=\"#confirmDialogue\" data-bs-whatever=\"delFn\" data-bs-id=\"root_{more_id}\">\u5220\u9664</div>\n</div>");
var parseSlideItem = function parseSlideItem() {
  var slideItems = document.querySelectorAll('.item');
  if (slideItems.length > 0) {
    slideItems.forEach(function (slideIt) {
      var moveStartX, moveEndX, differX;
      slideIt.on('touchstart', function (e) {
        return moveStartX = e.targetTouches[0].pageX;
      });
      slideIt.on('touchmove', function (e) {
        moveEndX = e.targetTouches[0].pageX;
        differX = moveEndX - moveStartX;
        if (differX > 50) {
          slideIt.css('margin-left', '0');
          slideIt.elect('.item-del').css('display', 'none');
        } else if (differX < -50) {
          slideIt.elect('.item-box').css('margin-left', '0');
          slideIt.css('margin-left', '-60px');
          slideIt.elect('.item-del').css('display', 'flex');
        }
      });
    });
  }
};
var docItemGenerator = function docItemGenerator(args) {
  var docComponent = '';
  var args_ = typeof args == 'string' ? JSON.parse(args) : args;
  for (var i in args_) {
    var temp = docItem.replaceAll('{doc}', args_[i]);
    var temp_id = Math.random() * 3.14159266;
    temp = temp.replaceAll('{more_id}', 'more_' + temp_id);
    docComponent += temp;
  }
  return docComponent;
};

/*----侧边栏文件列表组件-----*/

var main_map = new Map();
main_map.set('none', "\n<div class=\"p-5\"></div>\n<div class=\"d-flex justify-content-center flex-column align-items-center mt-5\">\n    <h4>\u672A\u6253\u5F00\u6587\u4EF6</h4>\n    <button class=\"btn text-primary mt-4\" style=\"background:white;opacity:.9;letter-spacing:.1rem\" data-bs-toggle=\"modal\" data-bs-target=\"#confirmDialogue\" data-bs-whatever=\"createFn\">\u521B\u5EFA\u65B0\u6587\u4EF6</button>\n    <button class=\"btn text-primary mt-2 visually-hidden\" style=\"background:white;opacity:.9;letter-spacing:.1rem\">\u5BFC\u5165\u65B0\u6587\u4EF6</button>\n    <button class=\"btn text-primary mt-2\" style=\"background:white;opacity:.9;letter-spacing:.1rem\" data-bs-toggle=\"offcanvas\" href=\"#sidebar\" role=\"button\" aria-controls=\"sidebar\">\u6253\u5F00\u65E7\u6587\u4EF6</button>\n    <p class=\"mt-3 text-black-50 ps-3\">\n    <span class=\"badge bg-info\">\u63D0\u793A</span>&nbsp;&nbsp;\u70B9\u51FB\u5217\u8868\u9879\uFF0C\u5BF9\u6587\u4EF6\u8FDB\u884C\u7F16\u8F91\u6216\u9884\u89C8\u3002\n    </p>\n</div>");
main_map.set('exist', "".concat(Header(), "\n").concat(Editor(), "\n").concat(Preview()));
var LoadProcessor = function LoadProcessor(configs) {
  var main_content = main_map.get(configs.loadState);
  var fileContent = configs.tile == '（未打开文件）' ? '' : java.direader(configs.tile, 'mind');
  setTimeout(function () {
    $(waitloading_id).innerHTML = main_content;
    if (configs.loadState == 'exist') reloadEditor(fileContent);
  }, 1000);
  console.log('fileContent=' + fileContent);
};
var SideConfig = function SideConfig(args) {
  var docs = args.list || {};
  var tile = args.tile;
  var docList = docItemGenerator(docs);
  SideBar.setHeader(tile);
  var docListDiv = docList.length > 0 ? docList : '<p class="p-3">这里什么都没有~orz</p>';
  SideBar.setMain(docListDiv);
  parseSlideItem();
};
var loadPage = function loadPage(obj) {
  LoadProcessor(obj);
  SideConfig(obj);
  anPutConfig('.configs', AnconfigObj.toJson());
};

//关闭页面函数
var closePage = function closePage() {
  if (AnconfigObj.loadState == 'exist') {
    AnconfigObj.close();
    loadPage(AnconfigObj);
    anPutConfig('.configs', AnconfigObj.toJson());
  }
};

//自动保存函数
var autosaveFn = function autosaveFn() {
  if (AnconfigObj.loadState == 'exist') {
    var contents = $('edit_area').value;
    anPutData(AnconfigObj.tile, contents);
    anPutConfig('.configs', AnconfigObj.toJson());
    java.ijbridge(JSON.stringify({
      command: 'infost',
      params: "数据保存成功！"
    }));
  }
};

//另存函数
var anosaveFn = function anosaveFn(id) {
  if (AnconfigObj.loadState == 'exist') {
    $(id).autoclick();
  }
};

//导出函数
var hexportFn = function hexportFn(id) {
  if (AnconfigObj.loadState == 'exist') {
    $(id).autoclick();
  }
};

/**实际代码执行部分*/
loadPage(AnconfigObj);
/**实际代码执行部分*/