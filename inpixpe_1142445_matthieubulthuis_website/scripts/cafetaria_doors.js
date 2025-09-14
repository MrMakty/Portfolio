function hashPassword(input) {
    return CryptoJS.SHA256(input).toString(CryptoJS.enc.Hex);
}

function checkPassword(hashedBlue) {
    const correctBlue = "16477688c0e00699c6cfa4497a3612d7e83c532062b64b250fed8908128ed548";

    if (
        hashedBlue === correctBlue
    ) {
        console.log("Gefeliciteerd! De code is correct, Je hebt het gedaan crewmate!");
        alert("Gefeliciteerd! De code is correct, Je hebt het gedaan crewmate!");
        window.open("cafetaria.html", "_blank").focus();
    }
}

function main() {
    const formbutton = document.getElementById("submit");

    formbutton.addEventListener("click", () => {
        let blue = document.getElementById("numbers").value

        let hashedBlue = hashPassword(blue)

        checkPassword(hashedBlue);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    console.log("Welkom pagina is geladen!");
    main();
});

function stars() {
    let e = document.createElement("spawn");
    e.setAttribute("class", "star");
    document.body.appendChild(e);
    e.style.top = Math.random() * innerHeight + "px";

    let size = Math.random() * 12;
    let duration = Math.random() * 3;

    e.style.fontSize = 12 + "px";
    e.style.animationDuration = 2 + duration + "s";
    setTimeout(function () {
        document.body.removeChild(e);
    }, 5000);
}

setInterval(function () {
    stars();
}, 50);