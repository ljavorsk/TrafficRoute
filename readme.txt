Predmet: IJA
Účel: Semestrálny projekt
Nazov projektu: Traffic route
Autori: Lukáš Javorský (xjavor20)
        Patrik Ondriga (xondri08)

Abstrakt:
    Projekt je určený na simuláciu mestskej hromadnej dopravy.
    Projekt je písaný v jazyku Java SE 8, grafické rozhranie je tvorené pomocou knižnice JavaFX.
    GUI návrh je uložený v layout.fxml a niektoré prvky GUI sú vytvárané za behu programu.
    Backend programu je v triedach Map.java a map_src/*.java.
    Projekt je prekladaný pomocou aplikácie ant.
    Preklad projektu je možné spravit príkazom 'ant compile' a spustenie pomocou 'ant run'.
    Generovanie projektovej dokumentácie sa spúšta pomocou príkazu 'ant doc'.

GUI:
    Pri spustení projektu sa zobrazí hlavné aplikačné okno, s názvom mesta, ktorého doprava je simulovaná.
    Ďalej sa zobrazujú všetky ulice a stopky, ktoré sa načítali zo súboru street.json.
    Taktiež sa načítavajú linky zo súboru line.json, ktoré majú na začiatku každá jeden autobus.
    V hornom pravom rohu, má užívateľ možnosť meniť rýchlosť času v simulácii, alebo ju kompletne zastaviť.
    Na Pravo v strede sa zobrazujú buď všetky ulice, alebo všetky linky. V pravej dolnej časti sú tlačitka
    na prepínanie medzi ulicami a linkami.

Linky:
    Pri rozkliknutí tlačidla pre linky sa užívateľovi zobrazia všetky linky v mape.
    Pri rozkliknutí špecifickej linky sa farba jej autobusov zmení na modrú a ich trasa sa vyfarbí na fialovo.
    Pokiaľ si užívateľ praje pridať autobus do linky, stačí rozkliknúť linku do ktorej chce pridávať a stlačiť
    zodpovedajú tlačidlo. Maximálne na jednej linke môžu byť 4 autobusy.
    Taktiež je možné autobusy odstrániť, ale minimálne jeden autobus ostane.
    Ak je niektorá z ulic cez ktorú linka prechádza uzavretá, je možné definovať obchádzku ktorou táto linka
    pôjde. Po kliknutí tlačítka na vytvorenie obchádzky sa zobrazí v pravej časti možnosti definície ulice,
    pre ktorú bude obchádzka definovaná a ulice cez ktoré obchádzka povedie. Vždy keď si užívateľ zvolí ulicu, 
    tak sa na mape zfarbý na červeno, alebo na zeleno. Červenou je zvýraznená ulica ktorá sa ide obchádzať
    a zelenou sú ulice, ktoré definujú obchádzku. Tieto nastavia môže užívateľ potvrdiť, alebo zrušiť.

Ulice:
    Pri rozkliknutí tlačidla pre ulice, sa vyfarbia zaťaženia ulíc podľa nastavených
    hodnôt (na začiatku sú všetky nastavené na 1, tj. najmenej zaťažené).
    Pokiaľ si praje užívateľ túto hodnotu zmeniť, pri rozkliknutí špecifickej ulice, sa mu zobrazí
    rozsah, z ktorého môže vyberať.
    Pri rozkliknutí špecifickej ulice, sa táto ulica zvýrazní, aby ju mohol užívateľ lepšie zbadať.
    Taktiež sa v nastaveniach pre ulicu nachádza tlačítko pre otvorenie, alebo zavretie ulicu. Ulica
    sa dá zavrieť iba v prípade, ak sa na ulici nenachádza žiadny autobus. Uzavretá ulica sa na mape zobrazuje
    čiernou farbou.