package map;

/**
 * Categories of map-objects, mainly map-labels.
 */
public enum Language {
    EN,
    LOCAL
}


/** OSM-lang-codes:
    AD	Andorra	ca
    AE	United Arab Emirates	ar
    AF	Afghanistan	fa, ps
    AG	Antigua and Barbuda	en
    AI	Anguilla	en
    AL	Shqipëria	sq
    AM	Armenia	hy
    AO	Angola	pt
    AQ	Antarctica
    AR	Argentina	es
    AS	American Samoa	en, sm
    AT	Österreich	de
    AU	Australia	en
    AW	Aruba	nl, pap
    AX	Aland Islands	sv
    AZ	Azerbaijan	az
    BA	Bosna i Hercegovina	bs, hr, sr
    BB	Barbados	en
    BD	Bangladesh	bn
    BE	België - Belgique - Belgien	nl, fr, de
    BF	Burkina Faso	fr
    BG	България	bg
    BH	البحرين	ar
    BI	Burundi	fr
    BJ	Bénin	fr
    BL	Saint Barthélemy	fr
    BM	Bermuda	en
    BN	Brunei Darussalam	ms
    BO	Bolivia	es, qu, ay
    BQ	Caribisch Nederland	nl
    BR	Brasil	pt
    BS	Bahamas	en
    BT	Bhutan	dz
    BV	Bouvet Island	no
    BW	Botswana	en, tn
    BY	Беларусь	be, ru
    BZ	Belize	en
    CA	Canada	en, fr
    CC	Cocos (Keeling) Islands	en
    CD	République Démocratique du Congo	fr
    CF	Centrafrique	fr
    CG	République du Congo	fr
    CH	Switzerland	de, fr, it, rm
    CI	Côte d'Ivoire	fr
    CK	Cook Islands	en, rar
    CL	Chile	es
    CM	Cameroon;Cameroun	fr, en
    CN	China 中国	zh
    CO	Colombia	es
    CR	Costa Rica	es
    CU	Cuba	es
    CV	Cabo Verde	pt
    CW	Curaçao	nl, en
    CX	Christmas Island	en
    CY	Κύπρος	el, tr
    CZ	Česká republika	cs
    DE	Deutschland	de
    DJ	جيبوتي;Djibouti	fr, ar, so
    DK	Danmark	da
    DM	Dominica	en
    DO	República Dominicana	es
    DZ	الجزائر	ar
    EC	Ecuador	es
    EE	Eesti	et
    EG	Egypt / مصر	ar
    EH	Western Sahara	ar, es, fr
    ER	Eritrea	ti, ar, en
    ES	España	ast, ca, es, eu, gl
    ET	Ethiopia	am, om
    FI	Suomi	fi, sv, se
    FJ	Fiji	en
    FK	Falkland Islands	en
    FM	Micronesia	en
    FO	Føroyar/Færøerne	fo
    FR	France	fr
    GA	Gabon	fr
    GB	United Kingdom	en, ga, cy, gd, kw
    GD	Grenada	en
    GE	Georgia / საქართველო	ka
    GF	Guyane Française	fr
    GG	Guernsey	en
    GH	Ghana	en
    GI	Gibraltar	en
    GL	Kalaallit Nunaat	kl, da
    GM	The Gambia	en
    GN	Guinée	fr
    GP	Guadeloupe	fr
    GQ	Equatorial Guinea	es, fr, pt
    GR	Ελλάδα	el
    GS	South Georgia and South Sandwich Islands	en
    GT	Guatemala	es
    GU	Guam	en, ch
    GW	Guiné-Bissau	pt
    GY	Guyana	en
    HK	Hong Kong	zh, en
    HM	Heard Island and McDonald Islands	en
    HN	Honduras	es
    HR	Hrvatska	hr
    HT	Haiti	fr, ht
    HU	Magyarország	hu
    ID	Indonesia	id
    IE	Ireland	en, ga
    IL	ישראל	he
    IM	Isle of Man	en
    IN	India	hi, en
    IO	British Indian Ocean Territory	en
    IQ	Iraq	ar, ku
    IR	ایران	fa
    IS	Ísland	is
    IT	Italia	it, de, fr
    JE	Jersey	en
    JM	Jamaica	en
    JO	Jordan / الأُرْدُن	ar
    JP	日本 (Japan)	ja
    KE	Kenya	sw, en
    KG	Kyrgyzstan	ky, ru
    KH	Cambodia	km
    KI	Kiribati	en
    KM	Comores;ﺍﻟﻘﻤﺮي;Komori	ar, fr
    KN	Saint Kitts and Nevis	en
    KP	북조선	ko
    KR	대한민국	ko, en
    KW	Kuwait / الكويت	ar
    KY	Cayman Islands	en
    KZ	Kazakhstan	kk, ru
    LA	Laos	lo
    LB	لبنان Lebanon	ar, fr
    LC	Saint Lucia	en
    LI	Liechtenstein	de
    LK	Sri Lanka	si, ta
    LR	Liberia	en
    LS	Lesotho	en, st
    LT	Lietuva	lt
    LU	Luxembourg	lb, fr, de
    LV	Latvija	lv
    LY	Libya / ليبيا	ar
    MA	Maroc	ar
    MC	Monaco	fr
    MD	Moldova	ru, uk, ro
    ME	Crna Gora	srp, sq, bs, hr, sr
    MF	Saint Martin	fr
    MG	Madagascar	mg, fr
    MH	Marshall Islands	en, mh
    MK	Македонија	mk
    ML	Mali	fr
    MM	Myanmar	my
    MN	Монгол Улс	mn
    MO	Macao	zh, pt
    MP	Northern Mariana Islands	ch
    MQ	Martinique	fr
    MR	موريتانيا	ar, fr
    MS	Montserrat	en
    MT	Malta	mt, en
    MU	Mauritius	mfe, fr, en
    MV	Maldives	dv
    MW	Malawi	en, ny
    MX	México	es
    MY	Malaysia	ms
    MZ	Mozambique	pt
    NA	Namibia	en, sf, de
    NC	Nouvelle-Calédonie	fr
    NE	Niger	fr
    NF	Norfolk Island	en, pih
    NG	Nigeria	en
    NI	Nicaragua	es
    NL	Nederland	nl
    NO	Norge – Noreg	nb, nn, no, se
    NP	Nepal	ne
    NR	Nauru	na, en
    NU	Niue	niu, en
    NZ	New Zealand	mi, en
    OM	سلطنة عُمان Oman	ar
    PA	Panama	es
    PE	Peru	es
    PF	Polynésie française	fr
    PG	Papua New Guinea	en, tpi, ho
    PH	Philippines	en, tl
    PK	پاکستان	en, ur
    PL	Polska	pl
    PM	Saint-Pierre-et-Miquelon	fr
    PN	Pitcairn	en, pih
    PR	Puerto Rico	es, en
    PS	Palestinian Territory	ar, he
    PT	Portugal	pt
    PW	Palau	en, pau, ja, sov, tox
    PY	Paraguay	es, gn
    QA	قطر Qatar	ar
    RE	Réunion	fr
    RO	România	ro
    RS	Србија (Serbia)	sr, sr-Latn
    RU	Россия	ru
    RW	Rwanda	rw, fr, en
    SA	Saudi Arabia / السعودية	ar
    SB	Solomon Islands	en
    SC	Seychelles	fr, en, crs
    SD	السودان ‎al-Sūdān	ar, en
    SE	Sverige	sv
    SG	Singapore	en, ms, zh, ta
    SH	Saint Helena	en
    SI	Slovenija	sl
    SJ	Svalbard and Jan Mayen	no
    SK	Slovensko	sk
    SL	Sierra Leone	en
    SM	San Marino	it
    SN	Sénégal	fr
    SO	Somalia / الصومال	so, ar
    SR	Suriname	nl
    ST	São Tomé e Príncipe	pt
    SS	South Sudan	en
    SV	El Salvador	es
    SX	Sint Maarten	nl, en
    SY	Sūriyya سوريا	ar
    SZ	Swaziland	en, ss
    TC	Turks and Caicos Islands	en
    TD	Tchad / تشاد	fr, ar
    TF	Terres australes et antarctiques françaises	fr
    TG	Togo	fr
    TH	ประเทศไทย	th
    TJ	Tajikistan	tg, ru
    TK	Tokelau	tkl, en, sm
    TL	Timor-Leste; Timor Lorosa'e	pt, tet
    TM	Türkmenistan	tk
    TN	تونس	ar
    TO	Tonga	en
    TR	Türkiye	tr
    TT	Trinidad and Tobago	en
    TV	Tuvalu	en
    TW	Taiwan	zh
    TZ	Tanzania	sw, en
    UA	Україна	uk
    UG	Uganda	en, sw
    UM	United States Minor Outlying Islands	en
    US	United States of America	en
    UY	Uruguay	es
    UZ	Uzbekistan	uz, kaa
    VA	Città del Vaticano	it
    VC	Saint Vincent and the Grenadines	en
    VE	Venezuela	es
    VG	British Virgin Islands	en
    VI	United States Virgin Islands	en
    VN	Việt Nam	vi
    VU	Vanuatu	bi, en, fr
    WF	Wallis-et-Futuna	fr
    WS	Samoa	sm, en
    YE	اليَمَن al-Yaman	ar
    YT	Mayotte	fr
    ZA	South Africa	zu, xh, af, st, tn, en
    ZM	Zambia	en
    ZW	Zimbabwe	en, sn, nd
*/
