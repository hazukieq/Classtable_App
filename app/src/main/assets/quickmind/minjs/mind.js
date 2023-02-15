"use strict";

//静态资源组
var hint_control_id = 'hint_control';
var edit_area_id = 'edit_area';
var edit_area_hint = '请在此输入内容...';
var resizable_area_id = 'resizable_area';
var edit_area_root_id = 'edit_area_root';
var svg_root_id = 'svg_root';
var svg_toolbar_id = 'svg_toolbar';
var edit_fullscreen_btn_id = "edit_fullscreen_btn";
var FullScreenEditor_id = "fullscreen_editor";
var fullscreen_edit_id = 'fullscreen_edit';
var input_modal_id = 'input_modal';
var help_modal_id = 'helpModal';
var markmap_height = window.screen.availHeight * 6 / 10 - 32;
var markmap_tool_height = 45;
var fullscreen_height = window.screen.availHeight * 8 / 10;
var grammar_help = '语法帮助';
var preview_mode_txt = '预览模式';
var edit_mode_txt = '编辑模式';
var non_contents = '暂无内容显示';
//静态资源组

var helpBtn = function helpBtn() {
  return "<button class=\"btn btn-outline-primary\" data-bs-toggle=\"modal\" data-bs-target=\"#".concat(help_modal_id, "\">\n    ").concat(grammar_help, "\n</button>");
};
var switchControl = function switchControl(switch_id) {
  $switch(switch_id, {
    controlEle: $(edit_area_root_id),
    hintEle: $(hint_control_id)
  }, function (ele, args) {
    args.controlEle.css('display', 'none');
    args.hintEle.innerHTML = preview_mode_txt;
  }, function (ele, args) {
    args.controlEle.css('display', 'block');
    args.hintEle.innerHTML = edit_mode_txt;
  });
};
var previeModeSwitch = function previeModeSwitch() {
  return "<div class=\"form-check form-switch\">\n        <input class=\"form-check-input\" type=\"checkbox\" id=\"flexswitch\" onclick=\"switchControl(this)\" isCheck=\"false\">\n        <label class=\"form-check-label\" for=\"flexswitch\" id=\"".concat(hint_control_id, "\">").concat(edit_mode_txt, "</label>\n    </div>");
};
var Header = function Header() {
  return "<div class=\"d-flex justify-content-end align-items-center\">\n        ".concat(previeModeSwitch(), "\n    </div>");
};
var textClear = function textClear(obj) {
  $(obj).value = '';
  $(svg_root_id).innerHTML = "<div class=\"text-center pt-5\"><h4>".concat(non_contents, "</h4></div>");
  $(svg_toolbar_id).innerHTML = '';
  $(edit_fullscreen_btn_id).attr('data-bs-whatever', '');
};
var confirmInput = function confirmInput() {
  var h = $('input_content').value;
  //console.log(typeof h)
  if (h !== null && typeof parseFloat(h) == 'number') $(edit_area_id).css('height', h > 48 ? h + 'px' : '48px');
};
var inputModal = function inputModal() {
  var modal = "<!-- Modal -->\n    <div class=\"modal fade\" id=\"".concat(input_modal_id, "\" tabindex=\"-1\" aria-labelledby=\"").concat(input_modal_id, "\" aria-hidden=\"true\">\n        <div class=\"modal-dialog modal-dialog-centered\">\n            <div class=\"modal-content\">\n                <div class=\"modal-body\">\n                    <h5>\u9AD8\u5EA6\u8C03\u6574</h5>\n                    <div class=\"pt-1 mt-1\">\n                        <p class=\"text-primary\">\u6700\u5C11\u9AD8\u5EA6\u4E3A48,\u4E0D\u80FD\u4F4E\u4E8E48,\u5426\u5219\u4E0D\u4F1A\u751F\u6548!</p>\n                        <div class=\"input-group mb-3\">\n                            <span class=\"input-group-text\" id=\"input_name\">\u9AD8\u5EA6\u5927\u5C0F</span>\n                            <input type=\"number\" class=\"form-control\" aria-describedby=\"input_name\" id=\"input_content\">\n                        </div>\n                    </div>\n\n                    <div class=\"d-flex justify-content-between align-items-center\">\n                        <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n                        <button type=\"button\" class=\"btn text-primary\" data-bs-dismiss=\"modal\" onclick=\"confirmInput()\">\u786E\u5B9A</button>\n                </div>\n                </div>\n            </div>\n        </div>\n    </div>");
  return modal;
};
var Editor = function Editor() {
  return "<div class=\"row mb-3\" id=\"edit_area_root\">\n    <div class=\"col-12 p-1\">\n    \n    <button class=\"btn btn-white text-primary float-start\" data-bs-toggle=\"modal\" data-bs-target=\"#".concat(FullScreenEditor_id, "\" id=\"").concat(edit_fullscreen_btn_id, "\" data-bs-whatever=\"\">\n        <img src=\"./icons/fullscreen.svg\" width=\"18\" height=\"18\"></img>\n    </button>\n    \n    <button class=\"btn text-center text-info\" data-bs-toggle=\"modal\" data-bs-target=\"#").concat(input_modal_id, "\">\n        <img src=\"./icons/expand.svg\" width=\"18\" height=\"18\"></img>\n    </button>\n    \n    <button class=\"btn btn-white text-danger float-end\" data-bs-toggle=\"modal\" data-bs-target=\"#confirmDialogue\" data-bs-whatever=\"askFn\">\n        <img src=\"./icons/trash.svg\" width=\"18\" height=\"18\"></img>\n    </button>\n    </div>\n    <div class=\"mt-1 col-12\">\n        <textarea style=\"word-break:break-all;\" class=\"form-control border-0\" placeholder=\"").concat(edit_area_hint, "\" style=\"caret-color:#007AFF;resize:none;height:96px\" id=\"").concat(edit_area_id, "\" rows=\"2\" oninput=\"inputListener(this)\"></textarea>\n        </div>\n</div>");
};
var timeoutID;
var inputListener = function inputListener(ele) {
  clearTimeout(timeoutID);
  timeoutID = setTimeout(function () {
    $(edit_fullscreen_btn_id).attr('data-bs-whatever', ele.value);
    MarkdownDynamic(svg_root_id, ele, {
      height: markmap_height
    });

    //写入文件中
    var contents = $('edit_area').value;
    anPutData(AnconfigObj.tile, contents);
  }, 640);
};
var reloadEditor = function reloadEditor(contents) {
  var ele = $(edit_area_id);
  ele.value = contents;
  $(edit_fullscreen_btn_id).attr('data-bs-whatever', ele.value);
  MarkdownDynamic(svg_root_id, ele, {
    height: markmap_height
  });
};
var confirmEdit = function confirmEdit() {
  var contentz = $(fullscreen_edit_id).value;
  $(edit_fullscreen_btn_id).attr('data-bs-whatever', contentz);
  $(edit_area_id).value = contentz;
  MarkdownDynamic(svg_root_id, $(edit_area_id), {
    height: markmap_height
  });

  //写入文件中
  anPutData(AnconfigObj.tile, contentz);
};
var FullScreenEditor = function FullScreenEditor() {
  var modal = "<!-- Modal -->\n    <div class=\"modal fade\" id=\"".concat(FullScreenEditor_id, "\" tabindex=\"-1\" aria-labelledby=\"").concat(FullScreenEditor_id, "\" aria-hidden=\"true\">\n        <div class=\"modal-dialog modal-fullscreen\">\n            <div class=\"modal-content\">\n                <div class=\"modal-body\">\n                    <div class=\"pt-4\"></div>\n                    <div class=\"d-flex justify-content-between align-items-center\">\n                        <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n                        <h5 class=\"modal-title\" id=\"exampleModalLabel\">\u5185\u5BB9\u7F16\u8F91\u533A</h5>\n                        <button type=\"button\" class=\"btn text-primary\" data-bs-dismiss=\"modal\" onclick=\"confirmEdit()\">\u786E\u5B9A</button>\n                    </div>\n                    <div class=\"pt-1 mt-1 border-1 border-top d-flex align-items-start\">\n                    <div style=\"margin-top:2px;width:1px;height:").concat(fullscreen_height, "px;background-color:gray;opacity:.5;\"></div>\n                    <textarea class=\"form-control border-0\" style=\"word-break:break-all;caret-color:#007AFF;resize:none;width:100%;height:").concat(fullscreen_height, "px\" placeholder=\"").concat(edit_area_hint, "\" id=\"").concat(fullscreen_edit_id, "\"></textarea>\n                    </div>\n                </div>\n            </div>\n        </div>\n    </div>");
  return modal;
};
var addEven = function addEven() {
  var exampleModal = $(FullScreenEditor_id);
  exampleModal.addEventListener('show.bs.modal', function (event) {
    // Button that triggered the modal
    var button = event.relatedTarget;

    // Extract info from data-bs-* attributes
    var recipient = button.getAttribute('data-bs-whatever');
    var modalEdit = exampleModal.elect('.form-control');
    modalEdit.value = recipient;
  });
};
var Preview = function Preview() {
  return "<!--\u4E3B\u754C\u9762\u6570\u636E\u8F7D\u5165\u533A-->\n    <div class=\"row mt-1 p-1\" id=\"mmp_root\">\n        <div class=\"col p-0\" id=\"mmp\">\n            <!--js\u8F7D\u5165\u9884\u5904\u7406\u6570\u636E-->\n            <div class=\"p-1 bg-white rounded-top\" id=\"svg_root_toolbar\" style=\"height:".concat(markmap_tool_height, "px\"></div>\n           <div id=\"svg_root\" class=\"bg-white rounded-bottom\" style=\"width: 100%;height:").concat(markmap_height, "px;\">\n                <div class=\"text-center pt-5\"><h4>\u6682\u65E0\u5185\u5BB9\u663E\u793A</h4></div>\n           </div>\n        </div>\n    </div>");
};
var confirmDialogue_id = 'confirmDialogue';
var confirmDialogue = function confirmDialogue() {
  return "<!-- Modal -->\n    <div class=\"modal fade\" id=\"".concat(confirmDialogue_id, "\" tabindex=\"-1\" aria-labelledby=\"").concat(confirmDialogue_id, "\" aria-hidden=\"true\">\n        <div class=\"modal-dialog modal-dialog-centered\">\n            <div class=\"modal-content\">\n                <div class=\"modal-body\">\n                <!--inject contents here-->\n                </div>\n            </div>\n        </div>\n    </div>");
};
var confirmDialogueInit = function confirmDialogueInit(clickM) {
  var exampleModal = $(confirmDialogue_id);
  exampleModal.on('show.bs.modal', function (e) {
    var buttonType = e.relatedTarget;
    var clickName = buttonType.getAttribute('data-bs-whatever');
    if (clickM.has(clickName)) {
      var fn = clickM.get(clickName);
      var mbody = exampleModal.elect('.modal-body');
      if (typeof fn == 'function') fn(mbody, buttonType);
    }
    //exampleModal.elect('.modal-body').innerHTML=content
  });

  exampleModal.on('hidden.bs.modal', function (e) {
    exampleModal.elect('.modal-body').innerHTML = '';
  });
};

//图片文件名    
//图片尺寸数
//图片背景色
//图片质量比
//放大级别数
var ExporterConfig = {
  markmapId: 'markmap',
  opts: {
    backgroundColor: 'white',
    encoderOptions: 1,
    scale: 4
  }
};

//const doc_tile=ExporterConfig.editEle.length>0?"_"+ExporterConfig.editEle.split("\n")[0].replace(" ","").replace("#",""):""
var Exporter = {
  savePdf: function savePdf(opts) {
    return saveSvgAsPdfUri($(ExporterConfig.markmapId), opts);
  },
  saveJpg: function saveJpg(opts) {
    return saveSvgAsJpgUri($(ExporterConfig.markmapId), opts);
  },
  savePng: function savePng(opts) {
    return saveSvgAsPngUri($(ExporterConfig.markmapId), opts);
  }
};

//document.body.innerHTML+=FullScreenEditor()
$('modal_loading').innerHTML += FullScreenEditor() + '<br/>' + inputModal() + '<br/>' + confirmDialogue();
addEven();
var confirmDialogueClickMap = new Map();
var saveFn = function saveFn() {
  var inputObj = $("saveFn_input_file");
  var vl = inputObj.value;
  var neo_name = vl + '.md';
  var contents = $('edit_area').value;
  var checkIsDuplicate = function checkIsDuplicate(neo) {
    var isSame = AnconfigObj.list.filter(function (it) {
      return it == neo;
    });
    return isSame.length > 0;
  };
  if (vl.length > 0) {
    if (!checkIsDuplicate(neo_name)) {
      AnconfigObj.copy(neo_name, contents);

      //重新加载侧边栏数据
      SideConfig(AnconfigObj);

      // 触发对象可以是任何元素或其他事件目标
      $('saveFn_input_click').autoclick();
    } else {
      var hintObj = $('saveFn_input_hint');
      hintObj.innerHTML = '文件名重复了，请重新输入！';
      hintObj.setAttribute('class', 'text-danger ps-2');
      setTimeout(function () {
        return hintObj.setAttribute('class', 'visually-hidden');
      }, 1000);
    }
  } else {
    var hintObj = $('saveFn_input_hint');
    hintObj.innerHTML = '文件名不能为空！';
    hintObj.setAttribute('class', 'text-danger ps-2');
    setTimeout(function () {
      return hintObj.setAttribute('class', 'visually-hidden');
    }, 1000);
  }
};
confirmDialogueClickMap.set('saveFn', function (ele, btn) {
  return ele.innerHTML = "<div class=\"row mb-2\">\n    <div class=\"col-12\">\n        <h5>\u65B0\u6587\u4EF6</h5>\n    </div>\n    <div class=\"col-12 text-black-50\">\n        \u662F\u5426\u5C06\u5F53\u524D\u9875\u9762\u6570\u636E\u5185\u5BB9\u5B58\u81F3\u65B0\u6587\u4EF6\u5185\uFF1F\n    </div>\n</div>\n<div class=\"row\">\n    <label for=\"saveFn_input_file\" class=\"col-sm-2 col-form-label\">\u6587\u4EF6\u540D</label>\n    <div class=\"col-sm-10\">\n    <input type=\"text\" class=\"form-control\" id=\"saveFn_input_file\" placeholder=\"\u8BF7\u8F93\u5165\u6587\u4EF6\u540D\">\n    </div>\n\n    <p id=\"saveFn_input_hint\" class=\"text-danger ps-2 visually-hidden\">\u6587\u4EF6\u540D\u4E0D\u80FD\u4E3A\u7A7A\uFF01</p>\n</div>\n<div class=\"row pt-2\">\n    <div class=\"col justify-content-end align-items-center d-flex\">\n        <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n        <button type=\"button\" class=\"btn text-primary\" onclick=\"saveFn()\">\u786E\u5B9A</button>\n    </div>\n</div>\n<button class=\"visually-hidden\" id=\"saveFn_input_click\" data-bs-dismiss=\"modal\"></button>\n";
});
confirmDialogueClickMap.set('askFn', function (ele, btn) {
  ele.innerHTML = "\n    <div class=\"row\">\n        <div class=\"col\">\n            \u662F\u5426\u6E05\u7A7A\u5F53\u524D\u7F16\u8F91\u6846\u5168\u90E8\u5185\u5BB9\uFF1F\n        </div>\n    </div>\n    <div class=\"row\">\n        <div class=\"col justify-content-end align-items-center d-flex\">\n            <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n            <button type=\"button\" class=\"btn text-primary\" data-bs-dismiss=\"modal\"  onclick=\"textClear('".concat(edit_area_id, "')\">\u786E\u5B9A</button>\n        </div>\n    </div>");
});
var createFn = function createFn() {
  var inputObj = $("createFn_input_file");
  var vl = inputObj.value;
  var neo_name = vl + '.md';
  var checkIsDuplicate = function checkIsDuplicate(neo) {
    var isSame = AnconfigObj.list.filter(function (it) {
      return it == neo;
    });
    return isSame.length > 0;
  };
  if (vl.length > 0) {
    if (!checkIsDuplicate(neo_name)) {
      AnconfigObj.add(neo_name);
      AnconfigObj.open(neo_name);
      loadPage(AnconfigObj);

      // 触发对象可以是任何元素或其他事件目标
      $('createFn_input_click').autoclick();
    } else {
      var hintObj = $('createFn_input_hint');
      hintObj.innerHTML = '文件名重复了，请重新输入！';
      hintObj.setAttribute('class', 'text-danger ps-2');
      setTimeout(function () {
        return hintObj.setAttribute('class', 'visually-hidden');
      }, 1000);
    }
  } else {
    var hintObj = $('createFn_input_hint');
    hintObj.innerHTML = '文件名不能为空！';
    hintObj.setAttribute('class', 'text-danger ps-2');
    setTimeout(function () {
      return hintObj.setAttribute('class', 'visually-hidden');
    }, 1000);
  }
};
confirmDialogueClickMap.set('createFn', function (ele, btn) {
  var md_old_name = "默认";
  ele.innerHTML = "\n    <div class=\"col-12 mb-2\">\n        <h5>\u65B0\u6587\u4EF6</h5>\n    </div>\n    <div class=\"row\">\n        <label for=\"createFn_input_file\" class=\"col-sm-2 col-form-label\">\u6587\u4EF6\u540D</label>\n        <div class=\"col-sm-10\">\n            <input type=\"text\" class=\"form-control\" id=\"createFn_input_file\" placeholder=\"\u8BF7\u8F93\u5165\u65B0\u7684\u6587\u4EF6\u540D\" value=\"".concat(md_old_name, "\">\n        </div>\n        \n        <p id=\"createFn_input_hint\" class=\"text-danger ps-2 visually-hidden\">\u6587\u4EF6\u540D\u4E0D\u80FD\u4E3A\u7A7A\uFF01</p>\n    </div>\n    <div class=\"row pt-2\">\n        <div class=\"col justify-content-end align-items-center d-flex\">\n            <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n            <button type=\"button\" class=\"btn text-primary\" onclick=\"createFn()\">\u786E\u5B9A</button>\n        </div>\n    </div>\n    <button class=\"visually-hidden\" id=\"createFn_input_click\" data-bs-dismiss=\"modal\" ></button>\n    ");
});
confirmDialogueClickMap.set('editFn', function (ele, btn) {
  var more_id = btn.getAttribute('data-bs-id');
  var md_old_name = $(more_id).getAttribute('data-bs-name');
  ele.innerHTML = "\n    <div class=\"row\">\n        <label for=\"editFn_input_file\" class=\"col-sm-2 col-form-label\">\u6587\u4EF6\u540D</label>\n        <div class=\"col-sm-10\">\n            <input type=\"text\" class=\"form-control\" id=\"editFn_input_file\" placeholder=\"\u8BF7\u8F93\u5165\u65B0\u7684\u6587\u4EF6\u540D\" value=\"".concat(md_old_name.replace('.md', ''), "\">\n        </div>\n        <p id=\"editFn_input_hint\" class=\"text-danger ps-2 visually-hidden\">\u6587\u4EF6\u540D\u4E0D\u80FD\u4E3A\u7A7A\uFF01</p>\n    </div>\n    <div class=\"row pt-2\">\n        <div class=\"col justify-content-end align-items-center d-flex\">\n            <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n            <button type=\"button\" class=\"btn text-primary\"  onclick=\"editDoc('").concat(more_id, "')\">\u786E\u5B9A</button>\n        </div>\n    </div>\n    <button class=\"visually-hidden\" id=\"editFn_input_click\" data-bs-dismiss=\"modal\" ></button>\n    ");
});
confirmDialogueClickMap.set('delFn', function (ele, btn) {
  var more_id = btn.getAttribute('data-bs-id');
  ele.innerHTML = "\n    <div class=\"row\">\n        <div class=\"col\">\n            \u662F\u5426\u5220\u9664\u5F53\u524D\u9009\u4E2D\u6587\u4EF6\uFF1F\n        </div>\n    </div>\n    <div class=\"row\">\n        <div class=\"col justify-content-end align-items-center d-flex\">\n            <button type=\"button\" class=\"btn text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n            <button type=\"button\" class=\"btn text-primary\" data-bs-dismiss=\"modal\"  onclick=\"delDoc('".concat(more_id, "')\">\u786E\u5B9A</button>\n        </div>\n    </div>\n    ");
});
var enableClickFn = function enableClickFn(thi) {
  thi["switch"]({
    el: $("exportFn_input_file")
  }, function (ele, args) {
    thi.innerHTML = "确定";
    args.el.removeAttribute('disabled');
  }, function (ele, args) {
    thi.innerHTML = "编辑";
    if (args.el.value.length == 0) args.el.value = '新文件名';
    args.el.attr('disabled', '');
  });
};
var selectPropFn = function selectPropFn(it, ele) {
  it["switch"]({
    els: $(ele).electAll('li')
  }, function (ele, args) {
    for (var i = 0; i < args.els.length; i++) args.els[i].setAttribute('class', 'list-inline-item badpan');
    ele.attr('class', 'list-inline-item badpaned');
  }, function (ele, args) {
    for (var i = 0; i < args.els.length; i++) args.els[i].setAttribute('class', 'list-inline-item badpan');
  });
};
var confirmExportFn = function confirmExportFn() {
  SideBar.setHexport($("exportFn_input_file").value);
  var firstargs = $('export_color').electAll('li');
  var firstarg = '';
  for (var i = 0; i < firstargs.length; i++) {
    if (firstargs[i].getAttribute('isCheck') == 'true') {
      firstarg = firstargs[i].innerHTML;
    }
  }
  var secondargs = $('export_quality').electAll('li');
  var secondarg = '';
  for (var _i = 0; _i < secondargs.length; _i++) {
    if (secondargs[_i].getAttribute('isCheck') == 'true') {
      secondarg = secondargs[_i].innerHTML;
    }
  }
  var thirdargs = $('export_zoom').electAll('li');
  var thirdarg = '';
  for (var j = 0; j < thirdargs.length; j++) {
    if (thirdargs[j].getAttribute('isCheck') == 'true') {
      thirdarg = thirdargs[j].innerHTML;
    }
  }
  var foargs = $('export_format').electAll('li');
  console.log(foargs);
  var foarg = '';
  for (var k = 0; k < foargs.length; k++) {
    if (foargs[k].getAttribute('isCheck') == 'true') foarg = foargs[k].innerHTML;
  }
  var confimap = new Map();
  confimap.set('低', 0.5);
  confimap.set('中', 0.8);
  confimap.set('高', 1.0);
  confimap.set('白色', 'white');
  confimap.set('透明', 'transparent');
  confimap.set('灰色', '#f1f3f5');
  confimap.set('PNG', 'png');
  confimap.set('JPG', 'jpg');
  confimap.set('PDF', 'pdf');
  var bgColor = confimap.get(firstarg) == undefined ? 'white' : confimap.get(firstarg);
  var encode = confimap.get(secondarg) == undefined ? 1.0 : confimap.get(secondarg);
  var scale_ = thirdarg == '' ? 3.0 : thirdarg;
  var opts = {
    backgroundColor: bgColor,
    encoderOptions: encode,
    scale: scale_
  };
  var format_ = confimap.get(foarg) == undefined ? 'png' : confimap.get(foarg);
  switch (format_) {
    case 'png':
      Exporter.savePng(opts);
      break;
    case 'jpg':
      Exporter.saveJpg(opts);
      break;
    case 'pdf':
      Exporter.savePdf(opts);
      break;
    default:
      Exporter.savePng(opts);
      break;
  }
  setTimeout(function () {
    $("exportFn_input_click").autoclick();
  }, 200);
};
confirmDialogueClickMap.set('exportFn', function (ele, btn) {
  //图片文件名    
  //图片清晰度 0.5低 0.8中 1高
  //图片背景色 white白色 transparent透明 黑色black
  //放大级别数 1 2 3 4 5
  //图片格式 png jpg pdf 
  var oldame = SideBar.getHeader();
  var contents = "\n    <h5>\u5BFC\u51FA\u6587\u4EF6</h5>\n    <div class=\"row\">\n        <label for=\"exportFn_input_file\" class=\"col-3 col-form-label ps-1 pe-1\">\u6587\u4EF6\u540D</label>\n        <div class=\"col-7 pe-0 ps-0\">\n            <input id=\"exportFn_input_file\" type=\"text\" class=\"p-1 form-control border-top-0 border-start-0 border-end-0 rounded-0 bg-white\" id=\"exportFn_input_file\" placeholder=\"\u8BF7\u8F93\u5165\u65B0\u7684\u6587\u4EF6\u540D\" value=\"".concat(oldame.replace('.md', ''), "\" disabled>\n        </div>\n        <button  class=\"p-0 m-0 col-2 btn text-danger\" onclick=\"enableClickFn(this)\" isCheck=\"false\">\u7F16\u8F91</button>\n    </div>\n\n    <br/>\n    <div class=\"row\">\n        <div class=\"col-4\">\n            \u56FE\u7247\u6E05\u6670\u5EA6\n        </div>\n        <div class=\"col-8\">\n            <ul class=\"list-inline\" id=\"export_quality\">\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_quality')\">\u4F4E</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_quality')\">\u4E2D</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_quality')\">\u9AD8</li>\n            </ul>\n        </div>\n    </div>\n\n    <div class=\"row\">\n        <label class=\"col-4\">\u56FE\u7247\u80CC\u666F\u8272</label>\n        <div class=\"col-8\">\n            <ul class=\"list-inline\" id=\"export_color\">\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_color')\">\u767D\u8272</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_color')\">\u900F\u660E</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_color')\">\u7070\u8272</li>\n            </ul>\n        </div>\n    </div>\n\n    <div class=\"row\">\n        <label class=\"col-4\">\u653E\u5927\u7EA7\u522B\u6570</label>\n        <div class=\"col-8\">\n            <ul class=\"list-inline\" id=\"export_zoom\">\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_zoom')\">1</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_zoom')\">3</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_zoom')\">5</li>\n            </ul>\n        </div>\n    </div>\n\n    <div class=\"row\">\n        <label class=\"col-4\">\u5BFC\u51FA\u4E4B\u683C\u5F0F</label>\n        <div class=\"col-8\">\n            <ul class=\"list-inline\" id=\"export_format\">\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_format')\">PNG</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_format')\">JPG</li>\n                <li class=\"list-inline-item badpan\" isCheck=\"false\" onclick=\"selectPropFn(this,'export_format')\">PDF</li>\n            </ul>\n        </div>\n    </div>\n\n    <div class=\"row\">\n        <button class=\"visually-hidden\" id=\"exportFn_input_click\" data-bs-dismiss=\"modal\"></button>\n        <div class=\"col justify-content-end align-items-center d-flex\">\n            <button class=\"btn btn-white text-danger\" data-bs-dismiss=\"modal\">\u53D6\u6D88</button>\n            <button class=\"btn btn-white text-primary\" onclick=\"confirmExportFn()\">\u786E\u5B9A</button>\n        </div>\n    </div>\n    ");
  ele.innerHTML = contents;
});
confirmDialogueInit(confirmDialogueClickMap);