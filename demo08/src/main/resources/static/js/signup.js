(function () {
  const form = document.getElementById("signupForm");
  if (!form) return;

  const email = document.getElementById("email");
  const name = document.getElementById("name");
  const password = document.getElementById("password");
  const emailError = document.getElementById("emailError");
  const passwordError = document.getElementById("passwordError");
  const nameError = document.getElementById("nameError");
  const errorSummary = document.getElementById("errorSummary");
  const toggleBtn = document.getElementById("togglePwd");
  const pwdStrengthText = document.getElementById("pwdStrengthText");

  function setError(el, msg) {
    if (!el) return;
    el.textContent = msg;
    el.style.display = msg ? "block" : "none";
  }

  function validateEmail(v) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
  }

  function passwordStrength(v) {
    if (!v) return 0;
    let score = 0;
    if (v.length >= 8) score++;
    if (/[0-9]/.test(v)) score++;
    if (/[a-z]/.test(v) && /[A-Z]/.test(v)) score++;
    if (/[^A-Za-z0-9]/.test(v)) score++;
    return score;
  }

  function strengthText(score) {
    switch (score) {
      case 0:
        return "—";
      case 1:
        return "Weak";
      case 2:
        return "Fair";
      case 3:
        return "Good";
      case 4:
        return "Strong";
      default:
        return "—";
    }
  }

  if (password) {
    password.addEventListener("input", function () {
      const s = passwordStrength(password.value);
      if (pwdStrengthText) pwdStrengthText.textContent = strengthText(s);
    });
  }

  if (toggleBtn && password) {
    toggleBtn.addEventListener("click", function (e) {
      e.preventDefault();
      password.type = password.type === "password" ? "text" : "password";
      toggleBtn.textContent = password.type === "password" ? "👁️" : "🙈";
    });
  }

  form.addEventListener("submit", function (e) {
    // clear previous
    setError(emailError, "");
    setError(passwordError, "");
    setError(nameError, "");
    if (errorSummary) {
      errorSummary.style.display = "none";
      errorSummary.textContent = "";
    }

    const errors = [];

    if (!name || !name.value.trim()) {
      setError(nameError, "Full name is required");
      errors.push({ field: name, msg: "Full name is required" });
    }

    if (!email || !validateEmail(email.value)) {
      setError(emailError, "Please enter a valid email address");
      errors.push({ field: email, msg: "Please enter a valid email address" });
    }

    const pwd = password ? password.value : "";
    if (!pwd || pwd.length < 8) {
      setError(passwordError, "Password must be at least 8 characters");
      errors.push({
        field: password,
        msg: "Password must be at least 8 characters",
      });
    }

    if (errors.length) {
      e.preventDefault();
      // focus first invalid
      const first = errors[0].field;
      if (first && typeof first.focus === "function") {
        first.focus();
      }
      if (errorSummary) {
        errorSummary.textContent = errors.map((x) => x.msg).join(" \n");
        errorSummary.style.display = "block";
        errorSummary.tabIndex = -1;
        errorSummary.focus();
      }
    }
  });
})();
