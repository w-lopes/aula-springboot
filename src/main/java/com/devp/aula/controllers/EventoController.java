package com.devp.aula.controllers;

import javax.validation.Valid;

import com.devp.aula.models.Convidado;
import com.devp.aula.models.Evento;
import com.devp.aula.repository.ConvidadoRepository;
import com.devp.aula.repository.EventoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository er;

    @Autowired
    private ConvidadoRepository cr;

    @RequestMapping("/eventos")
    public ModelAndView listaEventos(){
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();

        mv.addObject("eventos", eventos);
        return mv;
    }

    @RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public String form(){
        return "evento/cadastrarEvento";
    }

    @RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result, RedirectAttributes attributes){

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos.");
            return "redirect:/cadastrarEvento";
        }

        this.er.save(evento);

        attributes.addFlashAttribute("mensagem", "Evento adicionado com sucesso.");

        return "redirect:/cadastrarEvento";
    }

    @RequestMapping(value="/deletar", method=RequestMethod.GET)
    public String deletaEvento(long codigo) {
        Evento evento = this.er.findByCodigo(codigo);
        Iterable<Convidado> convidados= this.cr.findByEvento(evento);
        this.cr.deleteAll(convidados);
        this.er.delete(evento);
        return "redirect:/eventos";
    }

    @RequestMapping(value="/deletarConvidado", method=RequestMethod.GET)
    public String deletaConvidado(String rg) {
        Convidado convidado = this.cr.findByRg(rg);
        Evento evento = convidado.getEvento();
        this.cr.delete(convidado);
        long codigoEvento = evento.getCodigo();
        return "redirect:/" + codigoEvento;
    }

    @RequestMapping(value="/{codigo}", method=RequestMethod.GET)
    public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo){
        Evento evento = this.er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        Iterable<Convidado> convidados = this.cr.findByEvento(evento);

        mv.addObject("evento", evento);
        mv.addObject("convidados", convidados);

        return mv;
    }

    @RequestMapping(value="/{codigo}", method=RequestMethod.POST)
    public String detalhesEventoSave(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes){

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos.");
            return "redirect:/{codigo}";
        }

        Evento evento = this.er.findByCodigo(codigo);
        convidado.setEvento(evento);
        this.cr.save(convidado);
        ModelAndView mv = new ModelAndView("evento/detalhesEvento");
        mv.addObject("evento", evento);

        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso.");
        return "redirect:/{codigo}";
    }
}
