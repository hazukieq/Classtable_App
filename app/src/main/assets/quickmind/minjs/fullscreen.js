"use strict";

var fullM = function fullM() {
  var fmarkmap_tool_height = 45;
  var _markmap_height = window.screen.availHeight * 6 / 10 - 32;
  var fmarkmap_height = window.screen.availHeight * 9 / 10 - 32;
  var fullModal = $('helpModal');
  fullModal.on('show.bs.modal', function (e) {
    var mmp = $('mmp');
    var mmp_svg = mmp.querySelector('#markmap');
    mmp_svg.style.height = fmarkmap_height;
    var fmmp_root = $('fmmp_root');
    fmmp_root.innerHTML = '';
    fmmp_root.appendChild(mmp);
  });
  fullModal.on('hidden.bs.modal', function (e) {
    var mmp = $('mmp');
    var mmp_svg = mmp.querySelector('#markmap');
    mmp_svg.style.height = markmap_height;
    var mmp_root = $('mmp_root');
    var fmmp_root = $('fmmp_root');
    fmmp_root.innerHTML = '';
    mmp_root.appendChild(mmp);
  });
};
fullM();