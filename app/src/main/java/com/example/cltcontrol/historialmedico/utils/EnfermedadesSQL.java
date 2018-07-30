package com.example.cltcontrol.historialmedico.utils;

public class EnfermedadesSQL {

    public static final String REGISTRO_ENFERMEDADES = "INSERT INTO 'enfermedad' ('codigo','nombre','grupo') VALUES \n" +
            " ('A00','Cólera','|I1'),\n" +
            " ('A000','Colera debido a vibrio cholerae o1, biotipo cholerae',NULL),\n" +
            " ('A001','Colera debido a vibrio cholerae o1, biotipo el tor',NULL),\n" +
            " ('A009','Colera no especificado',NULL),\n" +
            " ('A01','Fiebres tifoidea y paratifoidea','|I1'),\n" +
            " ('A010','Fiebre tifoidea',NULL),\n" +
            " ('A011','Fiebre paratifoidea a',NULL),\n" +
            " ('A012','Fiebre paratifoidea b',NULL),\n" +
            " ('A013','Fiebre paratifoidea c',NULL),\n" +
            " ('A014','Fiebre paratifoidea, no especificada',NULL),\n" +
            " ('A02','Otras infecciones debidas a Salmonella','|I1'),\n" +
            " ('A020','Enteritis debida a salmonella',NULL),\n" +
            " ('A021','Septicemia debida a salmonella',NULL),\n" +
            " ('A022','Infecciones localizadas debida a salmonella',NULL),\n" +
            " ('A028','Otras infecciones especificadas como debidas a salmonella',NULL),\n" +
            " ('A029','Infeccion debida a salmonella no especificada',NULL),\n" +
            " ('A03','Shigelosis','|I1'),\n" +
            " ('A030','Shigelosis debida a shigella dysenteriae',NULL),\n" +
            " ('A031','Shigelosis debida a shigella flexneri',NULL),\n" +
            " ('A032','Shigelosis debida a shigella boydii',NULL),\n" +
            " ('A033','Shigelosis debida a shigella sonnei',NULL),\n" +
            " ('A038','Otras shigelosis',NULL),\n" +
            " ('A039','Shigelosis de tipo no especificado',NULL),\n" +
            " ('A04','Otras infecciones intestinales bacterianas','|I1'),\n" +
            " ('A040','Infeccion debida a escherichia coli enteropatogena',NULL),\n" +
            " ('A041','Infeccion debida a escherichia coli enterotoxigena',NULL),\n" +
            " ('A042','Infeccion debida a escherichia coli enteroinvasiva',NULL),\n" +
            " ('A043','Infeccion debida a escherichia coli enterohemorragica',NULL),\n" +
            " ('A044','Otras infecciones intestinales debidas a escherichia coli',NULL),\n" +
            " ('A045','Enteritis debida a campylobacter',NULL),\n" +
            " ('A046','Enteritis debida a yersinia enterocolitica',NULL),\n" +
            " ('A047','Enterocolitis debida a clostridium difficile',NULL),\n" +
            " ('A048','Otras infecciones intestinales bacterianas especificadas',NULL),\n" +
            " ('A049','Infeccion intestinal bacteriana, no especificada',NULL),\n" +
            " ('A05','Otras intoxicaciones alimentarias bacterias','|I1'),\n" +
            " ('A050','Intoxicacion alimentaria estafilococica',NULL),\n" +
            " ('A051','Botulismo',NULL),\n" +
            " ('A052','Intoxicacion alimentaria debida a clostridium perfringens [clostridium welchii]',NULL),\n" +
            " ('A053','Intoxicacion alimentaria debida a vibrio parahaemolyticus',NULL),\n" +
            " ('A054','Intoxicacion alimentaria debida a bacillus cereus',NULL),\n" +
            " ('A058','Otras intoxicaciones alimentarias debidas a bacterias especificadas',NULL),\n" +
            " ('A059','Intoxicacion alimentaria bacteriana, no especificada',NULL),\n" +
            " ('A06','Amebiasis','|I1'),\n" +
            " ('A060','Disenteria amebiana aguda',NULL),\n" +
            " ('A061','Amebiasis intestinal cronica',NULL),\n" +
            " ('A062','Colitis amebiana no disenterica',NULL),\n" +
            " ('A063','Ameboma intestinal',NULL),\n" +
            " ('A064','Absceso amebiano del higado',NULL),\n" +
            " ('A065','Absceso amebiano del pulmon (j99.8)',NULL),\n" +
            " ('A066','Absceso amebiano del cerebro (g07)',NULL),\n" +
            " ('A067','Amebiasis cutanea',NULL),\n" +
            " ('A068','Infeccion amebiana de otras localizaciones',NULL),\n" +
            " ('A069','Amebiasis, no especificada',NULL),\n" +
            " ('A07','Otras enfermedades intestinales debidas a protozoarios','|I1'),\n" +
            " ('A070','Balantidiasis',NULL),\n" +
            " ('A071','Giardiasis [lambliasis]',NULL),\n" +
            " ('A072','Criptosporidiosis',NULL),\n" +
            " ('A073','Isosporiasis',NULL),\n" +
            " ('A078','Otras enfermedades intestinales especificadas debidas a protozoarios',NULL),\n" +
            " ('A079','Enfermedad intestinal debida a protozoarios, no especificada',NULL),\n" +
            " ('A08','Infecciones intestinales debidas a virus y otros organismos especificados','|I1'),\n" +
            " ('A080','Enteritis debida a rotavirus',NULL),\n" +
            " ('A081','Gastroenteropatia aguda debida al agente de norwalk',NULL),\n" +
            " ('A082','Enteritis debida a adenovirus',NULL),\n" +
            " ('A083','Otras enteritis virales',NULL),\n" +
            " ('A084','Infeccion intestinal viral, sin otra especificacion',NULL),\n" +
            " ('A085','Otras infecciones intestinales especificadas',NULL),\n" +
            " ('A09','Diarrea y gastroenteritis de presunto origen infeccioso','|I1'),\n" +
            " ('A15','Tuberculosis respiratoria, confirmada bacteriológica e histologicamente','|I2'),\n" +
            " ('A150','Tuberculosis del pulmon, confirmada por hallazgo microscopico del bacilo tuberculoso en esputo, con o sin cultivo',NULL),\n" +
            " ('A151','Tuberculosis del pulmon, confirmada unicamente por cultivo',NULL),\n" +
            " ('A152','Tuberculosis del pulmon, confirmada histologicamente',NULL),\n" +
            " ('A153','Tuberculosis del pulmon , confirmada por medios no especificados',NULL),\n" +
            " ('A154','Tuberculosis de ganglios linfaticos intratoracicos, confirmada bacteriologica e histologicamente',NULL),\n" +
            " ('A155','Tuberculosis de laringe, traquea y bronquios, confirmada bacteriologica e histologicamente',NULL),\n" +
            " ('A156','Pleuresia tuberculosa, confirmada bacteriologica e histologicamente',NULL),\n" +
            " ('A157','Tuberculosis respiratoria primaria, confirmada bacteriologica e histologicamente',NULL),\n" +
            " ('A158','Otras tuberculosis respiratorias, confirmadas bacteriologica e histologicamente',NULL),\n" +
            " ('A159','Tuberculosis respiratoria no especificada, confirmada bacteriologicamente e histologicamente',NULL),\n" +
            " ('A16','Tuberculosis respiratoria, no confirmada bacteriologica e histologicamente','|I2'),\n" +
            " ('A160','Tuberculosis del pulmon, con examen bacteriologico e histologico negativos',NULL),\n" +
            " ('A161','Tuberculosis del pulmon, sin examen bacteriologico e histologico',NULL),\n" +
            " ('A162','Tuberculosis de pulmon, sin mencion de confirmacion bacteriologica o histologica',NULL),\n" +
            " ('A163','Tuberculosis de ganglios linfaticos intratoracicos, sin mencion de confirmacion bacteriologica o histologica',NULL),\n" +
            " ('A164','Tuberculosis de laringe, traquea y bronquios, sin mencion de confirmacion bacteriologica o histologica',NULL),\n" +
            " ('A165','Pleuresia tuberculosa, sin mencion de confirmacion bacteriologica e histologica',NULL),\n" +
            " ('A167','Tuberculosis respiratoria primaria, sin mencion de confirmacion bacteriologica e histologica',NULL),\n" +
            " ('A168','Otras tuberculosis respiratorias, sin mencion de confirmacion',NULL),\n" +
            " ('A169','Tuberculosis respiratoria no especificada, sin mencion de confirmacion bacteriologica o histologica',NULL),\n" +
            " ('A17','Tuberculosis del sistema nervioso','|I2'),\n" +
            " ('A170','Meningitis tuberculosa (g01)',NULL),\n" +
            " ('A171','Tuberculoma meningeo (g07)',NULL),\n" +
            " ('A178','Otras tuberculosis del sistema nervioso',NULL),\n" +
            " ('A179','Tuberculosis del sistema nervioso, no especificada (g99.8)',NULL),\n" +
            " ('A18','Tuberculosis de otros órganos','|I2'),\n" +
            " ('A180','Tuberculosis de huesos y articulaciones',NULL),\n" +
            " ('A181','Tuberculosis del aparato genitourinario',NULL),\n" +
            " ('A182','Linfadenopatia periferica tuberculosa',NULL),\n" +
            " ('A183','Tuberculosis de los intestinos, el peritoneo y los ganglios mesentericos',NULL),\n" +
            " ('A184','Tuberculosis de la piel y el tejido subcutaneo',NULL),\n" +
            " ('A185','Tuberculosis del ojo',NULL),\n" +
            " ('A186','Tuberculosis del oido',NULL),\n" +
            " ('A187','Tuberculosis de glandulas suprarrenales (e35.1)',NULL),\n" +
            " ('A188','Tuberculosis de otros organos especificados',NULL),\n" +
            " ('A19','Tuberculosis miliar','|I2'),\n" +
            " ('A190','Tuberculosis miliar aguda de un solo sitio especificado',NULL),\n" +
            " ('A191','Tuberculosis miliar aguda de sitios multiples',NULL),\n" +
            " ('A192','Tuberculosis miliar aguda, no especificada',NULL),\n" +
            " ('A198','Otras tuberculosis miliares',NULL),\n" +
            " ('A199','Tuberculosis miliar, sin otra especificacion',NULL),\n" +
            " ('A20','Peste','|I3'),\n" +
            " ('A200','Peste bubonica',NULL),\n" +
            " ('A201','Peste celulocutanea',NULL),\n" +
            " ('A202','Peste neumonica',NULL),\n" +
            " ('A203','Meningitis por peste',NULL),\n" +
            " ('A207','Peste septicemica',NULL),\n" +
            " ('A208','Otras formas de peste',NULL),\n" +
            " ('A209','Peste, no especificada',NULL),\n" +
            " ('A21','Tularemia','|I3'),\n" +
            " ('A210','Tularemia ulceroglandular',NULL),\n" +
            " ('A211','Tularemia oculoglandular',NULL),\n" +
            " ('A212','Tularemia pulmonar',NULL),\n" +
            " ('A213','Tularemia gastrointestinal',NULL),\n" +
            " ('A217','Tularemia generalizada',NULL),\n" +
            " ('A218','Otras formas de tularemia',NULL),\n" +
            " ('A219','Tularemia , no especificada',NULL),\n" +
            " ('A22','Carbunco (ántrax)','|I3'),\n" +
            " ('A220','Carbunco cutaneo',NULL),\n" +
            " ('A221','Carbunco pulmonar',NULL),\n" +
            " ('A222','Carbunco gastrointestinal',NULL),\n" +
            " ('A227','Carbunco septicemico',NULL),\n" +
            " ('A228','Otras formas de carbunco',NULL),\n" +
            " ('A229','Carbunco, no especificado',NULL),\n" +
            " ('A23','Brucelosis','|I3'),\n" +
            " ('A230','Brucelosis debida a brucella melitensis',NULL),\n" +
            " ('A231','Brucelosis debida a brucella abortus',NULL),\n" +
            " ('A232','Brucelosis debida a brucella suis',NULL),\n" +
            " ('A233','Brucelosis debida a brucella canis',NULL),\n" +
            " ('A238','Otras brucelosis',NULL),\n" +
            " ('A239','Brucelosis, no especificada',NULL),\n" +
            " ('A24','Muermo y melioidosis','|I3'),\n" +
            " ('A240','Muermo',NULL),\n" +
            " ('A241','Melioidosis aguda y fulminante',NULL),\n" +
            " ('A242','Melioidosis subaguda y cronica',NULL),\n" +
            " ('A243','Otras melioidosis',NULL),\n" +
            " ('A244','Melioidosis , no especificada',NULL),\n" +
            " ('A25','Fiebre por mordedura de rata','|I3'),\n" +
            " ('A250','Espirilosis',NULL),\n" +
            " ('A251','Estreptobacilosis',NULL),\n" +
            " ('A259','Fiebre por mordedura de rata, no especificada',NULL),\n" +
            " ('A26','Erisipeloide','|I3'),\n" +
            " ('A260','Erisipeloide cutaneo',NULL),\n" +
            " ('A267','Septicemia por erysipelothrix',NULL),\n" +
            " ('A268','Otras formas de erisipeloide',NULL),\n" +
            " ('A269','Erisipeloide, no especificada',NULL),\n" +
            " ('A27','Leptospirosis','|I3'),\n" +
            " ('A270','Leptospirosis icterohemorragica',NULL),\n" +
            " ('A278','Otras formas de leptospirosis',NULL),\n" +
            " ('A279','Leptospirosis, no especificada',NULL),\n" +
            " ('A28','Otras enfermedades zoonóticas bacterianas, no clasificadas en otra parte','|I3'),\n" +
            " ('A280','Pasteurelosis',NULL),\n" +
            " ('A281','Enfermedad por rasguño de gato',NULL),\n" +
            " ('A282','Yersiniosis extraintestinal',NULL),\n" +
            " ('A288','Otras enfermedades zoonoticas bacterianas especificadas, no clasificadas en otra parte',NULL),\n" +
            " ('A289','Enfermedad zoonotica bacteriana, sin otra especificacion',NULL),\n" +
            " ('A30','Lepra (enfermedad de Hansen)','|I4'),\n" +
            " ('A300','Lepra indeterminada',NULL),\n" +
            " ('A301','Lepra tuberculoide',NULL),\n" +
            " ('A302','Lepra tuberculoide limitrofe',NULL),\n" +
            " ('A303','Lepra limitrofe',NULL),\n" +
            " ('A304','Lepra lepromatosa limitrofe',NULL),\n" +
            " ('A305','Lepra lepromatosa',NULL),\n" +
            " ('A308','Otras formas de lepra',NULL),\n" +
            " ('A309','Lepra, no especificada',NULL),\n" +
            " ('A31','Infecciones debidas a otras micobacterias','|I4'),\n" +
            " ('A310','Infecciones por micobacterias pulmonares',NULL),\n" +
            " ('A311','Infeccion cutanea por micobacterias',NULL),\n" +
            " ('A318','Otras infecciones por micobacterias',NULL),\n" +
            " ('A319','Infeccion por micobacteria, no especificada',NULL),\n" +
            " ('A32','Listeriosis','|I4'),\n" +
            " ('A320','Listeriosis cutanea',NULL),\n" +
            " ('A321','Meningitis y meningoencefalitis listeriana',NULL),\n" +
            " ('A327','Septicemia listeriana',NULL),\n" +
            " ('A328','Otras formas de listeriosis',NULL),\n" +
            " ('A329','Listeriosis, no especificada',NULL),\n" +
            " ('A33','Tetanos neonatal','|I4'),\n" +
            " ('A34','Tetanos obstetrico','|I4'),\n" +
            " ('A35','Otros tetanos','|I4'),\n" +
            " ('A36','Difteria','|I4'),\n" +
            " ('A360','Difteria faringea',NULL),\n" +
            " ('A361','Difteria nasofaringea',NULL),\n" +
            " ('A362','Difteria laringea',NULL),\n" +
            " ('A363','Difteria cutanea',NULL),\n" +
            " ('A368','Otras difterias',NULL),\n" +
            " ('A369','Difteria, no especificada',NULL),\n" +
            " ('A37','Tos ferina (tos convulsiva)','|I4'),\n" +
            " ('A370','Tos ferina debida a bordetella pertussis',NULL),\n" +
            " ('A371','Tos ferina debida a bordetella parapertussis',NULL),\n" +
            " ('A378','Tos ferina debida a otras especies de bordetella',NULL),\n" +
            " ('A379','Tos ferina, no especificada',NULL),\n" +
            " ('A38','Escarlatina','|I4'),\n" +
            " ('A39','Infección meningocócica','|I4'),\n" +
            " ('A390','Meningitis meningococica (g01)',NULL),\n" +
            " ('A391','Sindrome de waterhouse',NULL),\n" +
            " ('A392','Meningococemia aguda',NULL),\n" +
            " ('A393','Meningococemia cronica',NULL),\n" +
            " ('A394','Meningococemia, no especificada',NULL),\n" +
            " ('A395','Enfermedad cardiaca debida a meningococo',NULL),\n" +
            " ('A398','Otras infecciones meningococicas',NULL),\n" +
            " ('A399','Infeccion meningococica, no especificada',NULL),\n" +
            " ('A40','Septicemia estreptocócica','|I4'),\n" +
            " ('A400','Septicemia debida a estreptococo, grupo a',NULL),\n" +
            " ('A401','Septicemia debida a estreptococo, grupo b',NULL),\n" +
            " ('A402','Septicemia debida a estreptococo, grupo d',NULL),\n" +
            " ('A403','Septicemia debida a streptococcus pneumoniae',NULL),\n" +
            " ('A408','Otras septicemias estreptococicas',NULL),\n" +
            " ('A409','Septicemia estreptococica, no especificada',NULL),\n" +
            " ('A41','Otras septicemias','|I4'),\n" +
            " ('A410','Septicemia debida staphylococcus aureus',NULL),\n" +
            " ('A411','Septicemia debida a otro estafilococo especificado',NULL),\n" +
            " ('A412','Septicemia debida a estafilococo no especificado',NULL),\n" +
            " ('A413','Septicemia debida a haemophilus influenzae',NULL),\n" +
            " ('A414','Septicemia debida a anaerobios',NULL),\n" +
            " ('A415','Septicemia debida a otros organismos gramnegativos',NULL),\n" +
            " ('A418','Otras septicemias especificadas',NULL),\n" +
            " ('A419','Septicemia, no especificada',NULL),\n" +
            " ('A42','Actinomicosis','|I4'),\n" +
            " ('A420','Actinomicosis pulmonar',NULL),\n" +
            " ('A421','Actinomicosis abdominal',NULL),\n" +
            " ('A422','Actinomicosis cervicofacial',NULL),\n" +
            " ('A427','Septicemia actinomicotica',NULL),\n" +
            " ('A428','Otras formas de actinomicosis',NULL),\n" +
            " ('A429','Actinomicosis, sin otra especificacion',NULL),\n" +
            " ('A43','Nocardiosis','|I4'),\n" +
            " ('A430','Nocardiosis pulmonar',NULL),\n" +
            " ('A431','Nocardiosis cutanea',NULL),\n" +
            " ('A438','Otras formas de nocardiosis',NULL),\n" +
            " ('A439','Norcardiosis , no especificada',NULL),\n" +
            " ('A44','Bartonelosis','|I4'),\n" +
            " ('A440','Bartonelosis sistemica',NULL),\n" +
            " ('A441','Bartonelosis cutanea y mucocutanea',NULL),\n" +
            " ('A448','Otras formas de bartonelosis',NULL),\n" +
            " ('A449','Bartonelosis, no especificada',NULL),\n" +
            " ('A46','Erisipela','|I4'),\n" +
            " ('A48','Otras enfermedades bacterianas, no clasificadas en otra parte','|I4'),\n" +
            " ('A480','Gangrena gaseosa',NULL),\n" +
            " ('A481','Enfermedad de los legionarios',NULL),\n" +
            " ('A482','Enfermedad de los legionarios no neumonica [fiebre de pontiac]',NULL),\n" +
            " ('A483','Sindrome de choque toxico',NULL),\n" +
            " ('A484','Fiebre purpurica brasileña',NULL),\n" +
            " ('A488','Otras enfermedades bacterianas especificadas',NULL),\n" +
            " ('A49','Infección bacteriana de sitio no especificado','|I4'),\n" +
            " ('A490','Infeccion estafilococica, sin otra especificacion',NULL),\n" +
            " ('A491','Infeccion estreptococica, sin otra especificacion',NULL),\n" +
            " ('A492','Infeccion por haemophilus influenzae, sin otra especificacion',NULL),\n" +
            " ('A493','Infeccion por micoplasma, sin otra especificacion',NULL),\n" +
            " ('A498','Otras infecciones bacterianas de sitio no especificado',NULL),\n" +
            " ('A499','Infeccion bacteriana, no especificada',NULL),\n" +
            " ('A500','Sifilis congenita precoz, sintomatica',NULL),\n" +
            " ('A501','Sifilis congenita precoz, latente',NULL),\n" +
            " ('A502','Sifilis congenita precoz sin otra especificacion',NULL),\n" +
            " ('A503','Oculopatia sifilitica congenita tardia',NULL),\n" +
            " ('A504','Neurosifilis congenita tardia [neurosifilis juvenil]',NULL),\n" +
            " ('A505','Otras formas de sifilis congenita tardia, sintomatica',NULL),\n" +
            " ('A506','Sifilis congenita tardia, latente',NULL),\n" +
            " ('A507','Sifilis congenita tardia, sin otra especificacion',NULL),\n" +
            " ('A509','Sifilis congenita, sin otra especificacion',NULL),\n" +
            " ('A510','Sifilis genital primaria',NULL),\n" +
            " ('A511','Sifilis primaria anal',NULL),\n" +
            " ('A512','Sifilis primaria en otros sitios',NULL),\n" +
            " ('A513','Sifilis secundaria de piel y membranas mucosas',NULL),\n" +
            " ('A514','Otras sifilis secundarias',NULL),\n" +
            " ('A515','Sifilis precoz, latente',NULL),\n" +
            " ('A519','Sifilis precoz, sin otra especificacion',NULL),\n" +
            " ('A52','Sífilis tardía','|I5'),\n" +
            " ('A520','Sifilis cardiovascular',NULL),\n" +
            " ('A521','Neurosifilis sintomatica',NULL),\n" +
            " ('A522','Neurosifilis asintomatica',NULL),\n" +
            " ('A523','Neurosifilis no especificada',NULL),\n" +
            " ('A527','Otras sifilis tardias sintomaticas',NULL),\n" +
            " ('A528','Sifilis tardia, latente',NULL),\n" +
            " ('A529','Sifilis tardia, no especificada',NULL),\n" +
            " ('A53','Otras sífilis y las no especificadas','|I5'),\n" +
            " ('A530','Sifilis latente, no especificada como precoz o tardia',NULL),\n" +
            " ('A539','Sifilis, no especificada',NULL),\n" +
            " ('A54','Infección gonocócica','|I5'),\n" +
            " ('A540','Infeccion gonococica del tracto genitourinario inferior sin absceso periuretral o de glandula accesoria',NULL),\n" +
            " ('A541','Infeccion gonococica del tracto genitourinario inferior con absceso periuretral y de glandulas accesorias',NULL),\n" +
            " ('A542','Pelviperitonitis gonococica y otras infecciones gonococicas genitourinarias',NULL),\n" +
            " ('A543','Infeccion gonococica del ojo',NULL),\n" +
            " ('A544','Infeccion gonococica del sistema osteomuscular',NULL),\n" +
            " ('A545','Faringitis gonococica',NULL),\n" +
            " ('A546','Infeccion gonococica del ano y del recto',NULL),\n" +
            " ('A548','Otras infecciones gonococicas',NULL),\n" +
            " ('A549','Infeccion, gonococica, no especificada',NULL),\n" +
            " ('A55','Linfogranuloma (venereo) por clamidias','|I5'),\n" +
            " ('A56','Otras enfermedades de transmisión sexual debidas a clamidias','|I5'),\n" +
            " ('A560','Infeccion del tracto genitourinario inferior debida a clamidias',NULL),\n" +
            " ('A561','Infeccion del pelviperitoneo y otros organos genitourinarios debida a clamidias',NULL),\n" +
            " ('A562','Infecciones del tracto genitourinario debidas a clamidias, sin otra especificacion',NULL),\n" +
            " ('A563','Infeccion del ano y del recto debida a clamidias',NULL),\n" +
            " ('A564','Infeccion de faringe debida a clamidias',NULL),\n" +
            " ('A568','Infeccion de transmision sexual de otros sitios debida a clamidias',NULL),\n" +
            " ('A57','Chancro blando','|I5'),\n" +
            " ('A58','Granuloma inguinal','|I5'),\n" +
            " ('A59','Tricomoniasis','|I5'),\n" +
            " ('A590','Tricomoniasis urogenital',NULL),\n" +
            " ('A598','Tricomoniasis de otros sitios',NULL),\n" +
            " ('A599','Tricomoniasis, no especificada',NULL),\n" +
            " ('A60','Infección anogenital debida a virus del herpes (herpes simple)','|I5'),\n" +
            " ('A600','Infeccion de genitales y trayecto urogenital y debida a virus del herpes [herpes simple]',NULL),\n" +
            " ('A601','Infeccion de la piel perianal y recto por virus del herpes simple',NULL),\n" +
            " ('A609','Infeccion anogenital por virus del herpes simple, sin otra especificacion',NULL),\n" +
            " ('A63','Otras enfermedades de transmision predominantemente sexual, no clasificadas en otrasa parte','|I5'),\n" +
            " ('A630','Verrugas (venereas) anogenitales',NULL),\n" +
            " ('A638','Otras enfermedades de transmision predominantemente sexual, especificadas',NULL),\n" +
            " ('A64','Enfermedad de transmision sexual no especificada','|I5'),\n" +
            " ('A65','Sifilis no venerea','|I6'),\n" +
            " ('A66','Frambesia','|I6'),\n" +
            " ('A660','Lesiones iniciales de frambesia',NULL),\n" +
            " ('A661','Lesiones papilomatosas multiples y frambesia con paso de cangrejo',NULL),\n" +
            " ('A662','Otras lesiones precoces de la piel en la frambesia',NULL),\n" +
            " ('A663','Hiperqueratosis de frambesia',NULL),\n" +
            " ('A664','Goma y ulceras de frambesia',NULL),\n" +
            " ('A665','Gangosa',NULL),\n" +
            " ('A666','Lesiones frambesicas de los huesos y de las articulaciones',NULL),\n" +
            " ('A667','Otras manifestaciones de frambesia',NULL),\n" +
            " ('A668','Frambesia latente',NULL),\n" +
            " ('A669','Frambesia , no especificada',NULL),\n" +
            " ('A67','Pinta (carate)','|I6'),\n" +
            " ('A670','Lesiones primarias de la pinta',NULL),\n" +
            " ('A671','Lesiones intermedias de la pinta',NULL),\n" +
            " ('A672','Lesiones tardias de la pinta',NULL),\n" +
            " ('A673','Lesiones mixtas de la pinta',NULL),\n" +
            " ('A679','Pinta, no especificada',NULL),\n" +
            " ('A68','Fiebres recurrentes','|I6'),\n" +
            " ('A680','Fiebre recurrente transmitida por piojos',NULL),\n" +
            " ('A681','Fiebre recurrente transmitida por garrapatas',NULL),\n" +
            " ('A689','Fiebre recurrente, no especificada',NULL),\n" +
            " ('A69','Otras infecciones causadas por espiroquetas','|I6'),\n" +
            " ('A690','Estomatitis ulcerativa necrotizante',NULL),\n" +
            " ('A691','Otras infecciones de vicent',NULL),\n" +
            " ('A692','Enfermedad de lyme',NULL),\n" +
            " ('A698','Otras infecciones especificadas por espiroquetas',NULL),\n" +
            " ('A699','Infeccion por espiroqueta, no especificada',NULL),\n" +
            " ('A70','Infeccion debida a chlamydia psittaci','|I7'),\n" +
            " ('A71','Tracoma','|I7'),\n" +
            " ('A710','Estado inicial de tracoma',NULL),\n" +
            " ('A711','Estado activo de tracoma',NULL),\n" +
            " ('A719','Tracoma, no especificado',NULL),\n" +
            " ('A74','Otras enfermedades causadas por clamidias','|I7'),\n" +
            " ('A740','Conjuntivitis por clamidias (h13.1)',NULL),\n" +
            " ('A748','Otras enfermedades por clamidias',NULL),\n" +
            " ('A749','Infeccion por clamidias, no especificada',NULL),\n" +
            " ('A75','Tifus','|I8'),\n" +
            " ('A750','Tifus epidemico debido a rickettsia prowazekii transmitido por piojos',NULL),\n" +
            " ('A751','Tifus recrudescente [enfermedad de brill]',NULL),\n" +
            " ('A752','Tifus debido a rickettsia typhi',NULL),\n" +
            " ('A753','Tifus debido a rickettsia tsutsugamushi',NULL),\n" +
            " ('A759','Tifus, no especificado',NULL),\n" +
            " ('A77','Fiebre maculosa (rickettsiosis transmitida por garrapatas)','|I8'),\n" +
            " ('A770','Fiebre maculosa debido a rickettsia rickettsii',NULL),\n" +
            " ('A771','Fiebre maculosa debido a rickettsia conorii',NULL),\n" +
            " ('A772','Fiebre maculosa debido a rickettsia siberica',NULL),\n" +
            " ('A773','Fiebre maculosa debido a rickettsia australis',NULL),\n" +
            " ('A778','Otras fiebres maculosas',NULL),\n" +
            " ('A779','Fiebre maculosa, no especificada',NULL),\n" +
            " ('A78','Fiebre q','|I8'),\n" +
            " ('A79','Otras rickettsiosis','|I8'),\n" +
            " ('A790','Fiebre de las trincheras',NULL),\n" +
            " ('A791','Rickettsiosis pustulosa debida a rickettsia akari',NULL),\n" +
            " ('A798','Otras rickettsiosis especificadas',NULL),\n" +
            " ('A799','Rickettsiosis, no especificada',NULL),\n" +
            " ('A80','Poliomielitis aguda','|I9'),\n" +
            " ('A800','Poliomielitis aguda paralitica, asociada a vacuna',NULL),\n" +
            " ('A801','Poliomielitis aguda paralitica debida a virus salvaje importado',NULL),\n" +
            " ('A802','Poliomielitis aguda paralitica debida a virus salvaje autoctono',NULL),\n" +
            " ('A803','Otras poliomielitis agudas paraliticas, y no las especificadas',NULL),\n" +
            " ('A804','Poliomielitis aguda no paralitica',NULL),\n" +
            " ('A809','Poliomielitis aguda, sin otra especificacion',NULL),\n" +
            " ('A81','Infecciones del sistema nervioso central por virus atipico','|I9'),\n" +
            " ('A810','Enfermedad de creutzfeldt',NULL),\n" +
            " ('A811','Panencefalitis esclerosante subaguda',NULL),\n" +
            " ('A812','Leucoencefalopatia multifocal progresiva',NULL),\n" +
            " ('A818','Otras infecciones del sistema nervioso por virus atipico',NULL),\n" +
            " ('A819','Infecciones del sistema nervioso central por virus atipico, sin otra especificacion',NULL),\n" +
            " ('A82','Rabia','|I9'),\n" +
            " ('A820','Rabia selvatica',NULL),\n" +
            " ('A821','Rabia urbana',NULL),\n" +
            " ('A829','Rabia, sin otra especificacion',NULL),\n" +
            " ('A83','Encefalitis viral transmitida por mosquitos','|I9'),\n" +
            " ('A830','Encefalitis japonesa',NULL),\n" +
            " ('A831','Encefalitis equina del oeste',NULL),\n" +
            " ('A832','Encefalitis equina del este',NULL),\n" +
            " ('A833','Encefalitis de san luis',NULL),\n" +
            " ('A834','Encefalitis australiana',NULL),\n" +
            " ('A835','Encefalitis de california',NULL),\n" +
            " ('A836','Enfermedad por virus rocio',NULL),\n" +
            " ('A838','Otras encefalitis virales transmitidas por mosquitos',NULL),\n" +
            " ('A839','Encefalitis viral transmitida por mosquitos, sin otra especificacion',NULL),\n" +
            " ('A84','Encefalitis viral transmitida por garrapatas','|I9'),\n" +
            " ('A840','Encefalitis del lejano oriente transmitida por garrapatas [encefalitis primaveroestival rusa]',NULL),\n" +
            " ('A841','Encefalitis centroeuropea transmitida por garrapatas',NULL),\n" +
            " ('A848','Otras encefalitis virales transmitidas por garrapatas',NULL),\n" +
            " ('A849','Encefalitis viral transmitida por garrapatas, sin otra especificacion',NULL),\n" +
            " ('A85','Otras encefalitis virales, no clasificadas en otra parte','|I9'),\n" +
            " ('A850','Encefalitis enteroviral (g05.1)',NULL),\n" +
            " ('A851','Encefalitis por adenovirus (g05.1)',NULL),\n" +
            " ('A852','Encefalitis viral transmitida por artropodos, sin otra especificacion',NULL),\n" +
            " ('A858','Otras encefalitis virales especificadas',NULL),\n" +
            " ('A86','Encefalitis viral, no especificada','|I9'),\n" +
            " ('A87','Meningitis viral','|I9'),\n" +
            " ('A870','Meningitis enteroviral (g02.0)',NULL),\n" +
            " ('A871','Meningitis debida a adenovirus (g02.0)',NULL),\n" +
            " ('A872','Coriomeningitis linfocitica',NULL),\n" +
            " ('A878','Otras meningitis virales',NULL),\n" +
            " ('A879','Meningitis viral, sin otra especificacion',NULL),\n" +
            " ('A88','Otras infecciones virales del sistema nervioso central, no clasificadas en otra parte','|I9'),\n" +
            " ('A880','Fiebre exantematica enteroviral [exantema de boston]',NULL),\n" +
            " ('A881','Vertigo epidemico',NULL),\n" +
            " ('A888','Otras infecciones virales especificadas del sistema nervioso central',NULL),\n" +
            " ('A89','Infeccion viral del sistema nervioso central, no especificada','|I9'),\n" +
            " ('A90','Fiebre del dengue [dengue clasico]','|I10'),\n" +
            " ('A91','Fiebre del dengue hemorragico','|I10'),\n" +
            " ('A92','Otras fiebres virales transmitidas por mosquitos','|I10'),\n" +
            " ('A920','Enfermedad por virus chikungunya',NULL),\n" +
            " ('A921','Fiebre de oÂ´nyong',NULL),\n" +
            " ('A922','Fiebre equina venezolana',NULL),\n" +
            " ('A923','Fiebre del oeste del nilo',NULL),\n" +
            " ('A924','Fiebre del valle del rift',NULL),\n" +
            " ('A928','Otras fiebres virales especificadas transmitidas por mosquitos',NULL),\n" +
            " ('A929','Fiebre viral transmitida por mosquito, sin otra especificacion',NULL),\n" +
            " ('A93','Otras fiebres virales transmitidas por artrópodos, no clasificadas en otra parte','|I10'),\n" +
            " ('A930','Enfermedad por virus de oropouche',NULL),\n" +
            " ('A931','Fiebre transmitida por flebotomos',NULL),\n" +
            " ('A932','Fiebre de colorado transmitida por garrapatas',NULL),\n" +
            " ('A938','Otras fiebres virales especificadas transmitidas por antropodos',NULL),\n" +
            " ('A94','Fiebre viral transmitida por artropodos, no especificada','|I10'),\n" +
            " ('A95','Fiebre amarilla','|I10'),\n" +
            " ('A950','Fiebre amarilla selvatica',NULL),\n" +
            " ('A951','Fiebre amarilla urbana',NULL),\n" +
            " ('A959','Fiebre amarilla, no especificada',NULL),\n" +
            " ('A96','Fiebre hemorrágica por arenavirus','|I10'),\n" +
            " ('A960','Fiebre hemorragica de junin',NULL),\n" +
            " ('A961','Fiebre hemorragica de machupo',NULL),\n" +
            " ('A962','Fiebre de lassa',NULL),\n" +
            " ('A968','Otras fiebres hemorragicas por arenavirus',NULL),\n" +
            " ('A969','Fiebre hemorragica por arenavirus, sin otra especificacion',NULL),\n" +
            " ('A98','Otras fiebres virales hemorrágicas, no clasificadas en otra parte','|I10'),\n" +
            " ('A980','Fiebre hemorragica de crimea',NULL),\n" +
            " ('A981','Fiebre hemorragica de omsk',NULL),\n" +
            " ('A982','Enfermedad de la selva de kyasanur',NULL),\n" +
            " ('A983','Enfermedad por el virus de marburg',NULL),\n" +
            " ('A984','Enfermedad por el virus de ebola',NULL),\n" +
            " ('A985','Fiebres hemorragicas con sindrome renal',NULL),\n" +
            " ('A988','Otras fiebres hemorragicas virales especificadas',NULL),\n" +
            " ('A99','Fiebre viral hemorragica, no especificada','|I10');";
}
