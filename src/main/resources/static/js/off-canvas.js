(function($) {
  'use strict';
  $(function() {
    // Đóng sidebar khi trang web được tải lên
    $('#sidebar').attr('aria-expanded', 'false');

    $('[data-toggle="minimize"]').on("click", function() {
      var sidebar = $('#sidebar');
      var isExpanded = sidebar.attr('aria-expanded') === 'true';

      if (isExpanded) {
        sidebar.attr('aria-expanded', 'false');
        // Thực hiện các thao tác đóng sidebar ở đây
      } else {
        sidebar.attr('aria-expanded', 'true');
        // Thực hiện các thao tác mở sidebar ở đây
      }
    });
  });
})(jQuery);
