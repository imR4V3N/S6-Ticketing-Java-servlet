document.addEventListener("DOMContentLoaded", function () {
    const close = document.querySelectorAll("#closePopup");
    const buttonNo = document.querySelectorAll("#confirmNo");

    close.forEach(button => {
        button.addEventListener("click", function () {
            let id = this.getAttribute("data-popup");
            let popup = document.getElementById(id);
            if (popup){
                popup.style.display = "flex";
            }
        });
    });

    // Masquer le popup quand on clique sur "No"
    buttonNo.forEach(closeBtn => {
        closeBtn.addEventListener("click", function () {
            this.parentElement.parentElement    .style.display = "none";
        });
    });
});
