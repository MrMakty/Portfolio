const weekData = {
    1: {
        summary: "<ul><li>Leerdoelen</li><li>Proces</li><li>Onderbouwde keuzeontwerpen</li><li>Realisatie</li><li>Samenwerking</li><li>Verdiepingsvragen</li></ul>Klik voor het hele verhaal",
        fullStory: "Leerdoelen<br><br>Ik heb 3 leerdoelen vastgesteld met gebruik van de SMART methode. De vragen die ik hiermee heb opgesteld zijn de volgende:<br><br><ul><li>Wat kan ik met javascript?</li><li>Werken vanuit minimal viable product (MVP)</li><li>Kunnen erkennen dat ik vastloop en weten welke stappen ik moet ondernemen als ik vastloop</li></ul><br><br>Proces<br><br>Het proces is begonnen met het vaststellemn van verschillende onderwerpen zoals het thema (Among Us), de doelgroep (12-16 jaar oud) en de puzzels (wires, numbers, reacto en Simon says)<br><br>Onderbouwde keuzeontwerpen<br><br>We hebben met de groep deze keuzes gemaakt aangezien we allemaal al redelijk wat weten van het spel en het een leuk concept vonden om mee aan de slag te gaan<br><br>Realisatie<br><br>We zijn in eerst instantie vooral bezig geweest met nhet ontwerpen van concepten. Zodra we het als groep eens zijn met deze concepten gaan we ze uitwerken.<br><br>Samenwerking<br><br>Wij zijn zelf een vreindengroep die goed samenwerkt. We houden welkaar veel in de gaten of alles lukt en blijven elkaar ondersteunen als we problemen hebben.<br><br>Verdiepingsvragen<br><br>Mijn verdiepingsvragen zijn als volgt:<ul><li>Welke belangrijke programmeertalen zijn goed te combineren met elkaar?</li><li>Heb ik meer interesse in front-end of back-end coderen?</li></ul><br><br>Lees het hele blog <a href='blog_week1.html'>hier</a>"
    },
    2: {
        summary: "<ul><li>Leerdoelen</li><li>Proces</li><li>Onderbouwde keuzeontwerpen</li><li>Realisatie</li><li>Samenwerking</li></ul>Klik voor het hele verhaal",
        fullStory: "Week 2<br><br><br>Lees het hele blog <a href='blog_week2.html'>hier</a>"
    },
    3: {
        summary: "<ul><li>Leerdoelen</li><li>Proces</li><li>Onderbouwde keuzeontwerpen</li><li>Realisatie</li><li>Samenwerking</li><li>Wat maakt een geod spel</li></ul>Klik voor het hele verhaal",
        fullStory: "Week 3<br><br><br>Lees het hele blog <a href='blog_week3.html'>hier</a>"
    },
    4: {
        summary: "<ul><li>Leerdoelen</li><li>Proces</li><li>Onderbouwde keuzeontwerpen</li><li>Realisatie</li><li>Samenwerking</li><li>Feedback individuele website</li></ul>Klik voor het hele verhaal",
        fullStory: "Week 4<br><br><br>Lees het hele blog <a href='blog_week4.html'>hier</a>"
    },
    5: {
        summary: "<ul><li>Leerdoelen</li><li>Proces</li><li>Onderbouwde keuzeontwerpen</li><li>Realisatie</li><li>Samenwerking</li><li>Uitgewerkte puzzel</li></ul>Klik voor het hele verhaal",
        fullStory: "Week 5<br><br><br>Lees het hele blog <a href='blog_week5.html'>hier</a>"
    },
};

const buttons = document.querySelectorAll('.blogbutton');
const display = document.getElementById('display');

const urlParams = new URLSearchParams(window.location.search);
const weekParam = urlParams.get('week');

if (weekParam && weekData[weekParam]) {
    display.innerHTML = `<p>${weekData[weekParam].fullStory}</p>`;
}

buttons.forEach(button => {
    button.addEventListener('mouseover', () => {
        const week = button.dataset.week;
        display.innerHTML = `<p>${weekData[week].summary}</p>`;
    });

    button.addEventListener('click', () => {
        const week = button.dataset.week;
        display.innerHTML = `<p>${weekData[week].fullStory}</p>`;
    });
});

const triggerArea = document.querySelector(".trigger-area");
const gifContainer = document.querySelector(".gif-container");

triggerArea.addEventListener("click", () => {
    gifContainer.classList.add("move");
    gifContainer.style.display = "block";

    gifContainer.addEventListener(
        "animationend",
        () => {
            gifContainer.classList.remove("move");
            gifContainer.style.display = "none";
        },
        { once: true }
    );
});