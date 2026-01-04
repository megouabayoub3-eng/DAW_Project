document.addEventListener("DOMContentLoaded", function () {
  document
    .querySelectorAll(
      'form[action*="/admin/approve/"] button, form[action*="/admin/reject/"] button'
    )
    .forEach(function (btn) {
      btn.addEventListener("click", function (evt) {
        var form = evt.target.closest("form");
        var action = form.getAttribute("action");
        if (action.includes("/approve/")) {
          if (!confirm("Approve this user?")) {
            evt.preventDefault();
          }
        } else if (action.includes("/reject/")) {
          if (
            !confirm(
              "Reject this user? This action can be reversed by re-approving."
            )
          ) {
            evt.preventDefault();
          }
        }
      });
    });
});
