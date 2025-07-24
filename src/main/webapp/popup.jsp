<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Modal popup THÀNH CÔNG -->
<div id="successModal" class="modal">
  <div class="modal-content success">
    <span class="checkmark">✔</span>
    <h2>Thao tác thành công!</h2>
    <p>Dữ liệu của bạn đã được liên kết với hệ cơ sở thành công!</p>
    <button onclick="closeModal()">OK</button>
  </div>
</div>

<!-- Modal popup THẤT BẠI -->
<div id="errorModal" class="modal">
  <div class="modal-content error">
    <span class="crossmark">✖</span>
    <h2>Thao tác thất bại!</h2>
    <p id="errorMessage">Đã có lỗi xảy ra. Vui lòng thử lại.</p>
    <div id="errorButtons">
      <button onclick="closeModal()">OK</button>
    </div>
  </div>
</div>


<style>
  .modal { display: none; position: fixed; z-index: 999; padding-top: 100px; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4); font-family: sans-serif; }
  .modal-content { background-color: #fff; margin: auto; padding: 30px; border-radius: 10px; width: 400px; text-align: center; box-shadow: 0 0 10px #ccc; }
  .modal-content.success .checkmark { color: #4CAF50; font-size: 48px; display: block; margin-bottom: 15px; }
  .modal-content.error .crossmark { color: #F44336; font-size: 48px; display: block; margin-bottom: 15px; }
  .modal-content button { background-color: #2196F3; color: white; padding: 10px 25px; border: none; border-radius: 5px; cursor: pointer; }
  #errorButtons {
  	display: flex;
  	justify-content: center;
  	gap: 10px; /* Khoảng cách giữa các nút */
  	margin-top: 15px;
	}
  
</style>

<script>
  function showSuccessModal() {
    document.getElementById('successModal').style.display = 'block';
  }

  function showErrorModalWithMessage(message, showLoginButton = false) {
    document.getElementById('errorMessage').textContent = message;
    const buttonsDiv = document.getElementById('errorButtons');

    // Reset nút (tránh thêm trùng khi reload)
    buttonsDiv.innerHTML = "";

    if (showLoginButton) {
      const loginBtn = document.createElement("button");
      loginBtn.textContent = "Đăng nhập";
      loginBtn.onclick = function () {
        window.location.href = "login";
      };
      buttonsDiv.appendChild(loginBtn);
    }

    const okBtn = document.createElement("button");
    okBtn.textContent = "OK";
    okBtn.onclick = closeModal;
    buttonsDiv.appendChild(okBtn);

    document.getElementById('errorModal').style.display = 'block';
  }

  function closeModal() {
    document.getElementById('successModal').style.display = 'none';
    document.getElementById('errorModal').style.display = 'none';
    if (history.pushState) {
      const url = window.location.protocol + "//" + window.location.host + window.location.pathname;
      history.pushState({}, null, url);
    }
  }

  window.onload = function () {
    const params = new URLSearchParams(window.location.search);
    const status = params.get("success");
    const reason = params.get("reason");

    if (status === "true") {
      showSuccessModal();
    } else if (status === "false") {
      switch (reason) {
        case "not_logged_in":
          showErrorModalWithMessage("Bạn chưa đăng nhập. Vui lòng đăng nhập để tiếp tục.", true);
          break;
        case "no_permission":
          showErrorModalWithMessage("Tài khoản của bạn không có quyền truy cập trang này.");
          break;
        default:
          showErrorModalWithMessage("Đã có lỗi xảy ra. Vui lòng thử lại.");
          break;
      }
    }
  }
</script>

