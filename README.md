### Note preliminari ###
1. Questo progetto in determinate parti risulta sessista. Ciò è dovuto alle consegne e non al mio pensiero, scusate.
2. La seguente relazione è stata trascritta dall'originale (scritta in LaTeX). Se notate errori per favore fatemelo presente.

Introduzione
============

Il lavoro si pone l’obiettivo di descrivere il simulatore della
battaglia dei sessi che è stato commissionato per il corso di
*Metodologie di programmazione* tenuto dal professore *Pietro
Cenciarelli*. L’applicazione è scritta in *Java* e tenta di replicare la
battaglia dei sessi descritta da *Richard Dawkins* ne *“Il gene
egoista”*.

La battaglia dei sessi
----------------------

Nel saggio del 1976 di *Richard Dawkins* viene teorizzata la battaglia
dei sessi nella quale si scontrano quattro tipologie di individui, due
maschili e due femminili. Le tipologie maschili prendono il nome di
*Morigerati* e *Avventurieri* che d’ora in poi chiameremo
rispettivamente M e A. Le tipologie femminili invece sono *Prudenti* e
*Spregiudicate* che d’ora in poi chiameremo P e S. Le popolazioni
composte da tali tipologie si riproducono secondo regole evolutive
derivate dalla tabella MAPS che rappresenta il premio evolutivo che un
individuo guadagna dopo l’interazione con un altro individuo di sesso
opposto.

La tabella MAPS
---------------

La tabella MAPS dipende da tre parametri *a*, *b* e *c* che
rappresentano rispettivamente il premio per aver diffuso il proprio
parco genetico, il costo necessario a crescere un figlio e il costo del
corteggiamento preliminare alla procreazione. La tabella MAPS sarà
dunque questa:

|     |                   M                  |     A      |
|-----|--------------------------------------|------------|
|**P**|![img](http://latex.codecogs.com/gif.latex?%28a-%5Cfrac%7Bb%7D%7B2%7D-c%2C%20a-%5Cfrac%7Bb%7D%7B2%7D-c%29)|![img](http://latex.codecogs.com/gif.latex?%280%2C%5C%3A%200%29) |
|**S**| ![img](http://latex.codecogs.com/gif.latex?%28a-%5Cfrac%7Bb%7D%7B2%7D%2C%20a-%5Cfrac%7Bb%7D%7B2%7D%29)   | ![img](http://latex.codecogs.com/gif.latex?%28a%2C%5C%3A%20a-b%29)|

I parametri scelti da *Dawkins* sono ![img](http://latex.codecogs.com/gif.latex?a%3D15%2C%5C%3Ab%3D20) e ![img](http://latex.codecogs.com/gif.latex?c%3D3). Otteniamo
quindi la tabella:

 |         |      M      |      A       |
 |---------|-------------|--------------|
 |  **P**  |![img](http://latex.codecogs.com/gif.latex?%282%2C%5C%3A%202%29)  | ![img](http://latex.codecogs.com/gif.latex?%280%2C%5C%3A%200%29)  |
 |  **S**  |![img](http://latex.codecogs.com/gif.latex?%285%2C%5C%3A%205%29)   |![img](http://latex.codecogs.com/gif.latex?%2815%2C%5C%3A%20-5%29)  |

Il nostro primo obiettivo sarà quello di raggiungere la stabilità con
questi valori. Per avere una previsione di quale sarà tale stabilità
andremo a risolvere due semplici equazioni nelle quali il termine ![img](http://latex.codecogs.com/gif.latex?N_I)
rappresenta il numero di individui di tipologia ![img](http://latex.codecogs.com/gif.latex?I) e ![img](http://latex.codecogs.com/gif.latex?P_e%28I%2CJ%29) indica
il premio evolutivo, estratto dalla tabella MAPS, di I dopo aver
interagito con J:

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20N_P%5Ctimes%20P_e%28M%2CP%29%20&plus;%20N_S%5Ctimes%20P_e%28M%2CS%29%20%3D%20N_P%5Ctimes%20P_e%28A%2CP%29%20&plus;%20N_S%5Ctimes%20P_e%28A%2CS%29%24%24)

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20N_M%5Ctimes%20P_e%28P%2CM%29%20&plus;%20N_A%5Ctimes%20P_e%28P%2CA%29%20%3D%20N_M%5Ctimes%20P_e%28S%2CM%29%20&plus;%20N_A%5Ctimes%20P_e%28S%2CA%29%24%24)

Dalle equazioni appena riportate si può affermare che la popolazione è
stabile quando ogni tipologia ha lo stesso guadagno medio della propria
“rivale” dello stesso sesso. Otteniamo così una formula per sapere quale
sarà il rapporto tra le tipologie rivali:

![img](http://latex.codecogs.com/gif.latex?%24%24%5Cfrac%7BN_P%7D%7BN_S%7D%3D%5Cfrac%7BP_e%28A%2CS%29-P_e%28M%2CS%29%7D%7BP_e%28M%2CP%29-P_e%28A%2CP%29%7D%24%24)

![img](http://latex.codecogs.com/gif.latex?%24%24%5Cfrac%7BN_M%7D%7BN_A%7D%3D%5Cfrac%7BP_e%28S%2CA%29-P_e%28P%2CA%29%7D%7BP_e%28P%2CM%29-P_e%28S%2CM%29%7D%24%24)

I rapporti di stabilità per i valori imposti da *Dawkins* sono quindi
![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_P%7D%7BN_S%7D%3D5) e ![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_M%7D%7BN_A%7D%3D%5Cfrac%7B5%7D%7B3%7D%24) che espressi in
altro modo sono ![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_P%7D%7BN_P&plus;N_S%7D%3D%5Cfrac%7B5%7D%7B6%7D%24) e ![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_M%7D%7BN_M&plus;N_A%7D%3D%5Cfrac%7B5%7D%7B8%7D).

Il simulatore visualizzerà tali rapporti sotto forma di numeri decimali e quindi assomiglieranno più a questi: 

![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BP%7D%7BP&plus;S%7D%3D0%2C8%5Coverline%7B3%7D) e ![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BM%7D%7BM&plus;A%7D%3D0%2C625).

*N.B.*
È importante rispettare i seguenti vincoli cambiando i valori di
![img](http://latex.codecogs.com/gif.latex?%24a%2Cb%20%5Ctext%7B%20e%20%7Dc%24):

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20c%20%3C%20a%20%3C%20b%5Cqquad%20a-%5Cfrac%7Bb%7D%7B2%7D-c%3E0%20%5Cqquad%20c%5Cneq%200%24%24)

Il modello
==========

Il modello adottato per implementare il simulatore prevede che ogni
individuo possa agire in completa libertà incontrando altri individui di
sesso opposto e generando figli con metà del proprio parco genetico e
metà del partner solo se entrambi gli individui sono “felici”. Esiste
inoltre la possibilità che un individuo muoia di morte naturale prima di
aver portato a termine la missione di far prosperare i propri geni.

La felicità di un individuo
---------------------------

Per capire quando un individuo è felice dobbiamo prima definire cosa è
un *rivale*.

Si dice **rivale** di un individuo ![img](http://latex.codecogs.com/gif.latex?I) che appartiene alla tipologia ![img](http://latex.codecogs.com/gif.latex?T)
qualsiasi individuo dello stesso sesso di ![img](http://latex.codecogs.com/gif.latex?I) ma di tipologia diversa da
![img](http://latex.codecogs.com/gif.latex?T).

La nozione di *rivale* ci serve per definire il concetto di *felicità di
un individuo*:

Un individuo si dice felice quando il suo guadagno medio è maggiore del
proprio rivale.

Per fare un esempio:

Se in un determinato momento la tipologia P costituisce il 45% di tutte
le donne, allora un individuo di tipo M avrà come guadagno medio:

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20G_m%28M%29%20%3D%200%2C45%20%5Ctimes%20P_e%28M%2CP%29%20&plus;%200%2C55%20%5Ctimes%20P_e%28M%2CS%29%24%24)
![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20G_m%28M%29%20%3D%200%2C45%20%5Ctimes%202%20&plus;%200%2C55%20%5Ctimes%205%20%3D%200%2C9%20&plus;%202%2C75%24%24)
![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20G_m%28M%29%20%3D%203%2C65%24%24)

Mentre un individuo di tipo A ha unguadagno medio:

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20G_m%28A%29%20%3D%200%2C45%20%5Ctimes%20P_e%28A%2CP%29%20&plus;%200%2C55%20%5Ctimes%20P_e%28A%2CS%29%24%24)
![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20G_m%28A%29%20%3D%200%2C45%20%5Ctimes%200%20&plus;%200%2C55%20%5Ctimes%2015%20%3D%200%20&plus;%208%2C25%24%24)
![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20G_m%28A%29%20%3D%208%2C25%24%24)

In questo caso l’individuo di tipo A è felice perché
![img](http://latex.codecogs.com/gif.latex?%24G_m%28A%29%3EG_m%28M%29%24) e un incontro con una donna (anch’essa felice) gli
frutterà un figlio.

Probabilità di morte di un individuo
------------------------------------

Secondo la teoria di *Dawkins* gli uomini, producendo gameti più
piccoli, hanno un investimento iniziale minore rispetto alla donna che
si vede costretta a mettere a disposizione gameti molto più grandi e in
numero minore. Questo porta l’uomo a cercare di produrre più gameti
possibili e spargere il seme, mentre porta la donna a cercare un
compagno affidabile che le assicuri la buona riuscita nella crescita di
un figlio. Proviamo a trascrivere questa differenza per rappresentare
l’“infelicità” di un individuo considerando il sesso dello stesso.

La probabilità che un individuo ![img](http://latex.codecogs.com/gif.latex?I) muoia è data dalla formula:

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20P_m%28I%29%20%3D%20%5Cbegin%7Bcases%7D%200%20%26%20%5Ctext%7Bse%20%7DI%5Ctext%7B%20%E8%20felice%7D%20%5C%5C%20%5Cfrac%7Bp_1%28%5Cfrac%7B%5Ctext%7Bnumero%20di%20incontri%7D%7D%7B10%7D%29&plus;p_2%28%5Cfrac%7B%5Ctext%7Bnumero%20di%20figli%7D%7D%7B2%7D%29%7D%7Bp_1&plus;p_2%7D%20%26%20%5Ctext%7Baltrimenti%7D%20%5Cend%7Bcases%7D%24%24)

dove ![img](http://latex.codecogs.com/gif.latex?%24p_1%5Ctext%7B%20e%20%7Dp_2%24) sono parametri dipendenti dal sesso di ![img](http://latex.codecogs.com/gif.latex?I).
Infatti nel caso ![img](http://latex.codecogs.com/gif.latex?I) sia uomo allora ![img](http://latex.codecogs.com/gif.latex?%24p_1%3D3%5Ctext%7B%20e%20%7Dp_2%3D1%24). Se invece
fosse donna avremmo ![img](http://latex.codecogs.com/gif.latex?%24p_1%3D1%5Ctext%7B%20e%20%7Dp_2%3D3%24).
In tal modo aver avuto più incontri pesa di più sulla salute di un uomo
di quanto peserebbe su quella di una donna e l’aver fatto più figli pesa
di più sulla salute di una donna di quanto non lo farebbe su quella di
un uomo.
Se questo passaggio suona strano e poco rigoroso è perché è nato da
osservazioni empiriche e non da un ragionamento completamente estraneo
all’implementazione finale del simulatore.

Geni e cromosomi
----------------

Come abbiamo già detto, la nascita di un nuovo individuo è dettata
dall’incontro di due individui felici che lasceranno al nuovo nato metà
dei propri geni ciascuno. Ma cosa è un gene? Abbiamo scelto di
rappresentare questo meccanismo con il modello più semplice di
ereditarietà genetica, ossia quello sviluppato da *Gregor Mendel*. Nel
modello di *Mendel* un fenotipo (che nel nostro caso equivale a quello
che noi chiamiamo tipologia o tipo di individuo) è dato dai *caratteri
genetici* all’interno del *gene* di un individuo.

Un gene ![img](http://latex.codecogs.com/gif.latex?g) è una coppia non ordinata di caratteri genetici:

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20g%20%3D%20%5C%7Bx%2Cy%5C%7D%24%24)

La sola definizione di gene non basta a rappresentare tutte e quattro le
tipologie di individui, soprattutto perché è importante distinguere i
fenotipi in base al sesso. Abbiamo scelto allora di racchiudere i geni
all’interno di un *cromosoma*:

Un cromosoma ![img](http://latex.codecogs.com/gif.latex?c) è una coppia ordinata di geni. Il primo rappresenta il
gene maschile, il secondo quello femminile. 

![img](http://latex.codecogs.com/gif.latex?%24%24%5C%20c%20%3D%20%28%5C%7Bx%2Cy%5C%7D%2C%5C%7Ba%2Cb%5C%7D%29%24%24)

Il carattere genetico può essere *recessivo* o *dominante* e
l’estrazione del fenotipo seguirà le *leggi di Mendel* che per brevità
non verranno riscritte in questo documento.
Per convenzione i caratteri dominanti saranno espressi con un simbolo
maiuscolo mentre quelli recessivi con uno minoscolo. La scelta di quali
geni sono recessivi e quali dominanti è irrilevante dal punto di vista
statistico ma la nostra scelta è caduta sul seguente schema:

       Morigerati -> M
     Avventurieri -> a
         Prudenti -> p
    Spregiudicate -> S

Prendiamo in esempio un individuo di tipo S. Il suo cromosoma potrebbe
essere ![img](http://latex.codecogs.com/gif.latex?%24c%3D%28%5C%7Ba%2Ca%5C%7D%2C%5C%7Bs%2Cs%5C%7D%29%24). Il genere dell’individuo (femminile)
attiva il secondo gene che corrisponde al fenotipo di S.

Un individuo ha un cromosoma ![img](http://latex.codecogs.com/gif.latex?%24c%3D%28%5C%7BM%2Ca%5C%7D%2C%5C%7BP%2CP%5C%7D%29%24), se fosse maschio il
suo tipo sarebbe ![img](http://latex.codecogs.com/gif.latex?M) perché il primo carattere domina sul secondo, se
fosse femmina il suo tipo sarebbe invece ![img](http://latex.codecogs.com/gif.latex?P).

Implementazione del modello
===========================

Ora che il modello è stato sufficientemente illustrato possiamo
dedicarci all’implementazione dello stesso in un’applicazione *Java*. Il
simulatore è stato concepito per essere utilizzato con un’interfaccia a
riga di comando.

Struttura generale del codice
-----------------------------

La classe principale è *Simulator* nella quale avvengono le impostazioni
del caso specifico della simulazione de “*la battaglia dei sessi*” e del
processo delle opzioni e degli argomenti passati come input dall’utente.
Il progredire della simulazione è controllato dalla classe *Population*
dalla quale è possibile anche ottenere in qualsiasi momento uno *stato*
rappresentato dalla classe *SimulationState*. Ogni individuo è
un’istanza della classe *Human* ed ha come campo un’istanza della classe
*Chromosome*.

Individui come thread
---------------------

Secondo il nostro modello ogni individuo è libero di agire in maniera
del tutto autonoma, cosa che si sposa bene con un approccio concorrente
all’implementazione del simulatore. La classe *Human* infatti implementa
l’interfaccia *Runnable* che permette al codice di ogni individuo di
essere eseguito da un thread creato ad hoc. Questa implementazione ha
però mostrato presto i suoi limiti in quanto nei primi test si andava
velocemente ad esaurire la memoria della macchina impedendo di creare
nuovi thread. La soluzione è stata trovata nella libreria standard di
*Java* che offre la classe *ThreadPoolExecutor*. Tale classe permette di
porre un limite superiore ai thread in esecuzione estraendoli da una
coda. La possibilità di impostare dinamicamente il limite superiore di
thread della classe ha permesso di eseguire gli individui dello stesso
tipo da una sola istanza di *ThreadPoolExecutor* con tale limite
impostato secondo le proporzioni delle popolazioni corrispondenti. Tale
accorgimento risolve due problemi riscontrati durante lo sviluppo:

1.  Se il numero massimo di thread è superato e una tipologia è
    nettamente in vantaggio sulle altre può capitare che tutti i thread
    disponibili siano occupati da individui della stessa tipologia
    bloccando la simulazione.

2.  Se la popolazione totale supera il numero massimo di thread
    potrebbero essere sfalsate le percentuali delle tipologie rispetto
    agli individui “attivi” nonostante la percentuale rispetto alla
    popolazione totale sia coerente con la simulazione.

Sincronizzazione dei thread
---------------------------

L’ipotetica cittadina dove avviene la nostra “battaglia dei sessi” ha
come rirtovo sociale un *Hotel* con all’interno un *Bar* dove le donne
sono solite aspettare un drink offerto e gli uomini offrire da bere.
L’incontro avviene in modo del tutto casuale in quanto la donna si siede
al bancone (in coda) e l’uomo può offrire da bere esclusivamente alla
prima donna in coda. Una volta che due individui si incontrano hanno un
*appuntamento*. Se entrambi gli individui durante un appuntamento sono
felici allora la donna ottiene un riferimento al cromosoma del partner
con il quale genererà un figlio.

La genetica in Java
-------------------

Implementare il modello genetico descritto precedentemente si è rivelato
abbastanza semplice poiché non ha richiesto particolari accorgimenti. La
classe *Chromosome* ha due campi di tipo *Gene*, che è una sottoclasse
di *Chromosome*, e un campo di tipo *Gender* che indica il sesso
dell’individuo e quindi il gene da “attivare” per estrarre il tipo. La
classe *Gene* invece ha due campi di tipo primitivo *char* che
rappresentano, appunto, i caratteri genetici che possono essere
maiuscoli o minuscoli in base alla dominanza del gene. Inoltre la classe
*Chromosome* conserva i riferimenti ai cromosomi dei genitori per poter
estrarre un albero genealogico al termine della simulazione.
Sia la classe *Chromosome* sia la classe *Gene* hanno un costruttore che
prende in input due istanze della stessa classe e serve a semplificare
il meccanismo di creazione di nuovi cromosomi con i geni ereditati dai
genitori. Per semplicità un individuo creato (non generato durante una
simulazione) avrà un cromosoma di default con solo un riferimento ad un
gene (quello attivo).

Il codice `Human h = new Human("M");` crea una nuova istanza di
**Human** con cromosoma `([MM][null])` mentre
`Human h = new Human("S");` ritorna un individuo con cromosoma
`([null][ss])`.

Il costruttore di *Chromosome* deve quindi prendere in considerazione
anche l’eventualità che uno dei genitori sia stato generato. In quel
caso l’intero gene attivo viene passato al figlio da entrambi i
genitori:

    public Chromosome(Chromosome father, Chromosome mother) {
        
        this.father = father;
        this.mother = mother;
        
        if(father.femaleGene == null || mother.maleGene == null) {
            this.maleGene = father.maleGene;
            this.femaleGene = mother.femaleGene;
        } else {
            this.maleGene = new Gene(father.maleGene,mother.maleGene);
            this.femaleGene = new Gene(father.femaleGene,mother.femaleGene);
        }
        
        if(Math.random()>.5) {
            this.gender = Gender.MALE;
        } else {
            this.gender = Gender.FEMALE;
        }
        
    }
        

Mentre in tutti gli altri casi i geni sono creati da questo costruttore
che fonde in maniera casuale i geni dei genitori:

    public Gene(Gene g1, Gene g2) {
        if(Math.random()>.5) {
            this.x1 = g1.x1;
        } else {
            this.x1 = g1.x2;
        }
        if(Math.random()>.5) {
            this.x2 = g2.x1;
        } else {
            this.x2 = g2.x2;
        }
    }
        

Il simulatore
=============

L’esecuzione del simulatore senza l’utilizzo di opzioni farà partire la
simulazione con i valori di *Dawkins* e con una popolazione iniziale
composta in egual misura da *Morigerati, Avventurieri, Prudenti *e*
Spregiudicate*. L’output a video sarà prodotto solo alla fine della
simulazione mostrando un grafico e varie informazioni utili ai test



     
                                                            |                     
                                       |                    |                     
                                       |                    |                     
                                       |                    |                     
                                       |                    |                     
                                       |                    |                     
                                       |                    |                     
                    |                  |                    |                     
                    |                  |                    |                     
                    |                  |                    |                     
                    |                  |                    |                     
                    |                  |                    |                     
                    |                  |                    |                    |
                    |                  |                    |                    |
                    |                  |                    |                    |
                    |                  |                    |                    |
             A: 19,32% 102       M: 31,63% 167       P: 40,72% 215        S: 8,33% 44 
              Threads A: 0        Threads M: 0        Threads P: 0        Threads S: 0
    population: 528
    Threads: 0
    Active Humans: 445
    M/A+M: 0,62 P/S+P: 0,83   Avg gain: A: 2,55 M: 2,51 P: 1,24 S: 1,21 
    Male: 2,52 Female: 1,24
    Exiting...
    Simulation time: 00:14

La simulazione è durata 14 secondi ed ha raggiunto la stabilità sperata:

![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BM%7D%7BM&plus;A%7D%3D0%2C62%5Capprox0%2C625%5Ctext%7B%20e%20%7D%5Cfrac%7BP%7D%7BP&plus;S%7D%3D0%2C83%5Capprox0%2C8%5Coverline%7B3%7D)
Il numero di thread attivi è zero perché la simulazione è terminata. Da
notare il guadagno medio che è molto simile tra gli uomini e tra le
donne, indice di stabilità dello stato raggiunto.

Opzioni del simulatore
----------------------

È possibile impostare il simulatore da riga di comando usando le varie
opzioni (per ottenere un elenco completo lanciare il programma con
l’opzione `--help`).

### L’opzione `--verbose`

L’opzione `--verbose` se presente tra gli argomenti passati alla classe
principale permette all’utente di seguire in tempo reale la simulazione
tramite output a schermo.




                  |                                                               
                  |                                         |                     
                  |                                         |                     
                  |                                         |                     
                  |                                         |                     
                  |                                         |                     
                  |                                         |                     
                  |                                         |                     
                  |                                         |                     
                  |                    |                    |                     
                  |                    |                    |                    |
                  |                    |                    |                    |
                  |                    |                    |                    |
                  |                    |                    |                    |
                  |                    |                    |                    |
                  |                    |                    |                    |
                  |                    |                    |                    |
           A: 35,09% 100        M: 16,84% 48         P: 33,33% 95         S: 14,74% 42 
           Threads A: 88        Threads M: 48        Threads P: 60        Threads S: 31
    population: 285
    Threads: 229
    Active Humans: 223
    M/A+M: 0,32 P/S+P: 0,69   Avg gain: A: 4,6 M: 2,92 P: 0,65 S: -1,76  
    Male: 4,05 Female: -0,09
    0%                                                                              100%
    ************************************************************

La barra del caricamento in basso mostra i passaggi evolutivi ancora
necessari prima di mostrare un nuovo stato.

### L’opzione `-p`

Grazie all’opzione `-p` è possibile impostare una popolazione iniziale
diversa da quella di default. È necessario inserire quattro numeri
interi dopo `-p` per inizializzare una popolazione con le percentuali
passate.

Lanciare il programma con il comando `java Simulator` è equivalente a
lanciarlo con `java Simulator -p 25 25 25 25` in quanto le proporzioni
di default sono M=25%,A=25%,P=25% e S=25%.

Il comando `java Simulator -p 30 20 45 5` inizializzerà una popolazione
iniziale composta così: M=30%,A=20%,P=45% e S=5%.

### L’opzione `-abc`

Con questa opzione è possibile settare i parametri a, b e c.
Insieme all’opzione `-m` è possibile visualizzare i valore della tabella
*MAPS* e la stabilità attesa senza far partire la simulazione.

Il comando `java Simulator -abc 12 15 2 -m` produce come output:

            P gains 0 with A
            P gains 3 with M
            A gains 0 with P
            A gains 12 with S
            S gains -3 with A
            S gains 5 with M
            M gains 3 with P
            M gains 5 with S

            M/M+A: 0.6
            P/P+S: 0.7

        

### L’opzione `-g`

La meccanica di nascita di nuovi individui seguendo le regole della
genetica ha prodotto la necessità di estrapolare i dati per osservare
l’evolversi dei singoli geni attraverso le generazioni. L’opzione `-g`
rende queste informazioni osservabili stampando un albero genealogico di
![img](http://latex.codecogs.com/gif.latex?n) generazioni in base all’intero passato dopo l’opzione. È possibile
anche passare dopo il numero di generazioni un booleano per attivare il
*pretty print* della classe *TreePrinter* gentilmente offerta
dall’utente *MightyPork* (*Ondřej Hruška*) sul sito
*stackoverflow.com*[^1]

Il comando `java Simulator -g 3` produce un output simile al seguente:


        |       |       |---P([Ma][pp])
        |       |---P([aM][pp])
        |       |       |---A([aa][pp])
        |---P([aa][pp])
        |       |       |---P([aa][pp])
        |       |---A([aa][pp])
        |       |       |---A([aa][pS])
    P([aa][pp])
        |       |       |---P([Ma][pp])
        |       |---P([MM][pp])
        |       |       |---M([Ma][pp])
        |---M([aM][pp])
        |       |       |---P([aM][pp])
        |       |---M([aM][pp])
        |       |       |---A([aa][pp])



Il comando `java Simulator -g 3 true` produce un output simile al
seguente: 

                                                        A([aa][pp])                                                          
                               ┌─────────────────────────────┴─────────────────────────┐                               
                          M([Ma][pp])                                              P([aa][pp])                          
                 ┌─────────────┴─────────────┐                           ┌─────────────┴─────────────┐               
            M([aM][pp])                 S([aa][Sp])                 A([aa][pp])                 P([Ma][pp])          
         ┌───────┴───────┐           ┌───────┴───────┐           ┌───────┴───────┐           ┌───────┴───────┐       
    A([aa][pp])     P([Ma][pp]) A([aa][Sp])     P([aa][pp]) A([aa][pp])     P([aa][pp]) M([Ma][pp])     P([aa][pp])



Conclusioni
===========

Limiti del modello
------------------

Il simulatore è stato testato con numerosi dati di input e si è rilevato
abbastanza affidabile nel complesso. Tuttavia alcuni dati di input non
portano a risultati corretti.

Impostiamo i valori a=30, b=50 e c=4 che ci danno la seguente
tabella *MAPS*:

 |     |        M      |       A       |
 |-----|---------------|---------------|
 |**P**|   ![img](http://latex.codecogs.com/gif.latex?%24%281%2C%5C%3A%201%29%24)  |  ![img](http://latex.codecogs.com/gif.latex?%24%280%2C%5C%3A%200%29%24)   |
 |**S**|   ![img](http://latex.codecogs.com/gif.latex?%24%285%2C%5C%3A%205%29%24)  | ![img](http://latex.codecogs.com/gif.latex?%24%2830%2C%5C%3A%20-20%29%24) |

I valori che le formule (1) e (2) ci forniscono sono
![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_M%7D%7BN_M&plus;N_A%7D%3D0%2C8%5Coverline%7B3%7D) e
![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_P%7D%7BN_P&plus;N_S%7D%5Capprox%200%2C96).

Due valori così alti però innescano un meccanismo non voluto nel quale
*Prudenti* e *Avventurieri* continuano ad essere felici e generare figli
finché ![img](http://latex.codecogs.com/gif.latex?%5Cfrac%7BN_P%7D%7BN_P&plus;N_S%7D%3E0%2C96) che è di fatto la condizione di
felicità dei *Morigerati*. A questo punto il grandissimo numero di
*Avventurieri* presente nella popolazione porta l’estinguersi della già
esigua popolazione di *Spregiudicate* prima che i *Morigerati* possano
raggiungere la stabilità.

Possibili modifiche
-------------------

Il problema più grave del modello adottato è sicuramente il calcolo
della *felicità* di un individuo che è legato allo stato della
popolazione e non alle esperienze dei singoli individui. In una
precedente formulazione del modello infatti la felicità di un individuo
era ricavata da ![img](http://latex.codecogs.com/gif.latex?n) incontri passati. Tale approccio, però, si è
rivelato troppo poco accurato in quanto nel momento in cui il singolo
individuo completava la raccolta dei dati relativi ai suoi incontri,
tali dati risultavano già “obsoleti” rispetto all’evoluzione repentina
del totale della popolazione. Non escludiamo che si possa migliorare
tale dinamica implementando un sistema che possa attenuare questa
differenza rendendo il simulatore maggiormante corretto da un punto di
vista genetico e biologico.

[^1]: http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram

