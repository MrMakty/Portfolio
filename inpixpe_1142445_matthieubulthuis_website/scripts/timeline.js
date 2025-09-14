const weekData = {
    1: {
        summary: "<ul><li>Groep gemaakt</li><li>Onderwerp gekozen</li><li>Begin plan van aanpak</li><li>Concepten puzzels bedacht</li></ul>Klik voor het hele verhaal",
        fullStory: "In de eerste week heb ik een groep gemaakt zoals eerder al te lezen was op de eerste pagina.<br>Na een brainstormsessie met zijn vieren kwamen wij tot het idee om Among Us als thema aan te houden. Wij kozen er ook voor om de doelgroep van 12-16 jaar oud te gebruiken omdat dit het beste past bij het werkelijke spel van Among Us.<br>In deze week hebben we ook een begin gemaakt met het plan van aanpak en een paar concepten voor de puzzels bedacht. Het thema wat wij kozen voor de kleuren was een combinatie tussen 'ruimte' en de kleuren van de avatars die wij hadden gekozen."
    },
    2: {
        summary: "<ul><li>Leerdoelen</li><li>Brainstormen</li><li>Knopen doorhakken</li></ul>Klik voor het hele verhaal",
        fullStory: "Dit was de week na de vakantie. In de vakantie heb ik gewerkt aan een van mijn leerdoelen. Namelijk het meer leren van Javascript. Hiervoor heb ik een cursus online gekocht die ik aan het doen ben zodat ik mijn kennis van Javascript verbreed.<br><br>In deze week zijn we als groep hard bezig geweest met hete maken van keuzes. We hebben een brainstormsessie gehad van en de resultaten daarvan heb ik opgenomen als afbeelding in mijn blog.<br><br>Tijdens deze brainstorm hebben we als groep knopen doorgehakt en keuzes gemaakt over hoe onze groepswebsite eruit kwam te zien. Hierdoor kregen wij een beter beeld van hoe we verder konden gaan enw aar we naartoe aan het werken waren"
    },
    3: {
        summary: "<ul><li>Leerdoelen</li><li>Feedback op een goed spel</li><li>Vergadering leiden</li></ul>Klik voor het hele verhaal",
        fullStory: "In deze week heb ik mij meer gefocust op het leerdoel 'opleveren van een MVP'. Ik ben dus meer aan het focussen geweest op de minimale eisen van een project en heb zoveel mogelijk ideeen die ik had, maar niet perse aan de opdracht voldeden van me af geschoven. Hierdoor kon ik mij beter focussen op wat nodig was.<br><br>Tijdens de lessen kregen we feedback over wat een goed spel zou moeten zijn. Hierdoor kwamen wij met een groep tot de conclusie dat naar ons idee een goed spel de speler tot nadenken zet en dat een stukje geluk bij kan zitten om het spel wat interresanter te maken.<br><br>In deze week heb ik een vergadering genotuleerd. Ik heb screenshots van mijn notule toegevoegd aan mijn weekblog. Daarnaast heb ik ook direct daarna een agenda opgesteld die ik naar de rest van mijn projectgroep heb gestuurd, gecombineerd met een uitnodiging voor die vergadering."
    },
    4: {
        summary: "<ul><li>Leerdoelen</li><li>Hardware</li><li>Feedback mijn website</li></ul>Klik voor het hele verhaal",
        fullStory: "In deze week heb ik de focus gelegd op het leerdoel 'Erkennen wanneer ik vastloop'. Ik ben hiermee begonnen door een exceldocument op te stellen waarin ik verschillende dingen bij heb gehouden die mij hielpen met analyseren van mijn werkmethode. Hieruit kwam naar voren dat ik mij het beste voel en het beste presteer als ik snel om hulp vraag als ik vastloop. De vormen van hulp liepen vrij erg uiteen. De ene keer vroeg ik om hulp van ChatGPT en de andere keer vroeg ik het aan een medestudent.<br><br>Ik ben in deze week begonnen met het maken van de hardwareopdrachten die ik nodig had om mijn puzzel te maken. De voortgang hiervan heb ik opgenomen in mijn blog. Voor mij voelde het raar om aan het werk te gaan met electronica aangezien ik dat nog nooit eerder had gedaan. Gelukkig zijn er genoeg manieren hoe je op internet informatie hierover kan krijgen (hint hint naar het zojuist besproken leerdoel).<br><br>In deze week kregen we feedback op het prototype van onze individuele website. Ik had zeer zwaar onderschat wat we moesten inleveren hiervoor dus de feedback was niet extreem nuttig. Als nog heb ik deze geprobeerd zo geod mogelijk te verwerken en ik heb een screenshot van de feedback opgenomen in mijn blog."
    },
    5: {
        summary: "Afronden van onderdelen<br><br>Klik voor het hele verhaal",
        fullStory: "Dit is de laatste echte werkweek van het project. In deze week moet alles zo geod als af zijn. In deze week werden de laatste knopen doorgehakt wat betreft de afmetingen van de omhulsels waar de puzzels in komen. Deze worden op maat gemaakt en zijn van hout. Daarna zijn er gaten in de deksels gebord voor de displays, knoppen en overige onderdelen van de hardware en zijn daarna zwart geverfd.<br><img src='styles/assets/images/zwarte_omhulsels.jpg' alt='Geverfde dozen' class='dozen'/>"
    },
};

const buttons = document.querySelectorAll('.weekbutton');
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