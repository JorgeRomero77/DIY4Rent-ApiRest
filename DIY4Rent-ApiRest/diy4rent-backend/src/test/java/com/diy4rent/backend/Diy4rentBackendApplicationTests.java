package com.diy4rent.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;


import com.diy4rent.backend.model.Herramienta;
import com.diy4rent.backend.repository.HerramientaRepository;

import com.diy4rent.backend.model.Usuario;
import com.diy4rent.backend.repository.UsuarioRepository;

@SpringBootTest
class Diy4rentBackendApplicationTests {
	
    @Autowired
    private HerramientaRepository herramientaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

	@Test
	void testHerramienta() {
		
	Herramienta herramienta = new Herramienta();
	herramienta.setId(1);
        herramienta.setNombre("Pelacables con ajuste automático Weidmüller");
        herramienta.setCategoria("Pelacables");
        herramienta.setEstadoDesgaste(2);
        herramienta.setUserId(1);
        herramienta.setAlqAct(0);
        herramienta.setPrecio(2.5);
        herramienta.setDescripcion("Con el alicate pelacables automático STRIPAX - 9005000000 de Weidmüller se pueden pelar fácilmente conductores de hilo fino o múltiple con sección desde 0,08 hasta 10 mm² hasta un largo máximo de 25 mm.");
        herramienta.setFoto("https://media.automation24.com/Artikelbilder/Shop800px/102862_1.jpg");

      
        herramientaRepository.save(herramienta);
        
        Optional<Herramienta> herramienta2 = herramientaRepository.findById((long) 1);
        assertEquals(herramienta2.get().getNombre(), herramienta.getNombre());
        assertEquals(herramienta2.get().getNombre(), "Pelacables con ajuste automático Weidmüller");
        
        herramienta.setEstadoDesgaste(1);
        herramientaRepository.save(herramienta);
        
        herramienta2 = herramientaRepository.findById((long) 1);
        assertNotEquals(herramienta2.get().getEstadoDesgaste(), 2);
        assertEquals(herramienta2.get().getEstadoDesgaste(), 1);
        
        
        herramientaRepository.delete(herramienta);
        herramienta2 = herramientaRepository.findById((long) 1);
        assertFalse(herramienta2.isPresent());
		
	}

}
