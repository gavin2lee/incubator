(function () {
  var note = {
    deptCode: ay.getLocalData('deptCode'),
    init: function () {
      this.gettemplateList().then(function (res) {
        var options = '';

        if (res.rslt === '0') {
          options = res.data.result.map(function (resItem, i) {
            return ['<option ', (function () {
              if (i === 0) {
                return 'selected="selected"'
              }
            })(),' value="', resItem, '">', resItem, '</option>'].join('')
          }).join('');

          $('#whiteBoardTemplate').html(options);
        }
      })
      .then(this.getData)
      .then(this.parseForm);

      this.setTemplateId();

      $('#save').on('click', this.getFormData);
    },
    gettemplateList: function () {
      return $.get(ay.contextPath + '/nur/nurseWhiteBoard/queryTempIdByDeptCode', {
        deptCode: note.deptCode
      });
    },
    getData: function () {
      return $.get(ay.contextPath + '/nur/nurseWhiteBoard/getNwbRecordDtos', {
        deptCode: note.deptCode,
        templateId: $('#whiteBoardTemplate option:selected').val()
      })
    },

    setTemplateId: function () {
      $tid = $('#whiteBoardTemplate option:selected');

      note.tId = $tid.val();

      $tid.on('change', function () {
        note.tId = $tid.val()
      })
    },

    parseForm: function (res) {
      debugger;
      if (res.rslt !== '0' || !res.data) {
        return;
      }
      var data = res.data.map;
      var htmlArr = [];
      var prop;
      var $ul = $('.form-list');

      $ul.html('');

      for (prop in data) {
        if (data.hasOwnProperty(prop)) {
          htmlArr.push(['<li data-rid="', data[prop][0].recordId || '','" data-pid="', data[prop][0].patId || '','" data-code="', data[prop][0].code, '" class="white-board-item-v1" data-recordname="', prop,'">',
          '<span class="label-span"><strong>', prop,'</strong>：</span>',
          '<div class="input-div-v1">',
          '<textarea>',
          (function() {
            if (data[prop]) {
              return data[prop].map(function (recordItem) {
                return recordItem.patInfo || recordItem.value || '';
              }).join('')
            }
          })(),
          '</textarea>',
          '</div>'
        ].join(''))
        }
      }

      $ul.html(htmlArr.join(''));
    },

    getFormData: function () {
      var $li = $('.form-list li');
      var formDataArr = [];

      $li.each(function () {
        var $this = $(this);
        var prop;
        var objTemp = {
          recordId: $this.data('rid'),
          code: $this.data('code'),
          name: $this.find('.label-span strong').text(),
          deptCode: note.deptCode,
          templateId: note.tId,
          value: $this.find('textarea').val(),
          patId: $this.data('pid')
        };

        if (objTemp.recordId == '') {
          delete objTemp.recordId;
        }

        if (objTemp.patId == '') {
          delete objTemp.patId;
        }

        formDataArr.push(objTemp);

      });

      note.save(formDataArr)
    },

    save: function (dataArr) {
      $.post(ay.contextPath + '/nur/nurseWhiteBoard/saveNwbRecord', {
        nwbRecords: JSON.stringify(dataArr)
      }).done(function (res) {
        if (res.rslt === '0') {
          $.messager.alert('提示', res.msg || '保存成功！');
        } else {
          $.messager.alert('提示', res.msg || '保存失败！');
        }
      })
      .fail(function () {
        $.messager.alert('警告', '服务器错误！');
      })
    }
  };

  $(function () {
    note.init()
  })
})()
