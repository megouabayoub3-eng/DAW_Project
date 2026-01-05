// ui.js - handles loading states and dark mode toggle
(function () {
  // Dark mode toggle: remember choice in localStorage
  const darkToggle = document.getElementById("darkToggle");
  function applyDarkMode(enabled) {
    if (enabled) document.body.classList.add("dark");
    else document.body.classList.remove("dark");
  }
  try {
    const saved = localStorage.getItem("dark_mode");
    applyDarkMode(saved === "1");
  } catch (e) {}
  // ensure initial label matches current state
  if (darkToggle) {
    darkToggle.textContent = document.body.classList.contains("dark")
      ? "Light"
      : "Dark";
    darkToggle.addEventListener("click", function () {
      const enabled = !document.body.classList.contains("dark");
      applyDarkMode(enabled);
      try {
        localStorage.setItem("dark_mode", enabled ? "1" : "0");
      } catch (e) {}
      darkToggle.textContent = enabled ? "Light" : "Dark";
    });
  }

  // Attach loading spinner to forms/buttons with data-loading attribute
  document.querySelectorAll("button[data-loading]").forEach(function (btn) {
    const form = btn.closest("form");
    if (!form) return;
    form.addEventListener("submit", function (e) {
      // disable all buttons in the form
      form.querySelectorAll("button").forEach(function (b) {
        b.disabled = true;
      });
      // show spinner inside the clicked button
      const spinner = document.createElement("span");
      spinner.className = "spinner";
      btn.appendChild(spinner);
    });
  });
})();
