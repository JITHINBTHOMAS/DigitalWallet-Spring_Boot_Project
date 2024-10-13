package com.ust.wallet.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.wallet.model.Document;
import com.ust.wallet.model.Members;
import com.ust.wallet.service.WalletService;

@RestController
@RequestMapping("/member")
public class WalletController {
	@Autowired
    private WalletService service;

	@PostMapping("/addmember")
    public Members addMember(@RequestBody Members member){
		member.getDocuments().forEach(doc->doc.setMember(member));
        service.saveMember(member);
        member.getDocuments().forEach(doc->service.saveDocument(doc));
        return member;
    }

    @GetMapping("/getAll")
    public List<Members> getAllMembers() {
        return service.getAllMembers();
    }

    @GetMapping("/{id}")
    public Members getMember(@PathVariable Long id) {
        return service.getMemberById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        service.deleteMember(id);
        return "Deleted";
    }
    
    @PutMapping("/update")
    public Members updateMember(@RequestBody Members member) {
    	if(getMember(member.getId())!=null)
    		return service.saveMember(member);
    	else
    		return null;
    }
    
    @PutMapping("/update/{id}/{filename}")
    public Members updateOne(@PathVariable Long id,@PathVariable String filename,@RequestBody Document doc) {
    	Members member =  service.getMemberById(id);
        Optional<Document> optional = member.getDocuments()
				.stream()
				.filter(object->object.getFilename().equals(filename))
				.findFirst();
        member.getDocuments().remove(optional.get());
        service.saveDocument(doc);
        member.getDocuments().add(doc);
        return addMember(member);
        
    }
    
    @PutMapping("/addDocument/{id}")
    public Members addOneDocument(@PathVariable Long id,@RequestBody Document doc) {
    	Members member =  service.getMemberById(id);
    	doc.setMember(member);
    	member.getDocuments().add(doc);
        member.getDocuments().forEach(docs->service.saveDocument(docs));
        return service.saveMember(member);
    }
    
    @DeleteMapping("/{id}/{filename}")
    public Members deleteDocument(@PathVariable Long id,@PathVariable String filename) {
    	Members member =  service.getMemberById(id);
        Optional<Document> optional = member.getDocuments()
				.stream()
				.filter(object->object.getFilename().equals(filename))
				.findFirst();
        member.getDocuments().remove(optional.get());
//        service.saveMember(member);
        service.deleteDocument(optional.get().getId());
        return addMember(member);
//        service.deleteDocument(optional.get().getId());
//    	return "deleted";
    }
    @GetMapping("/{id}/{filename}")
    public Document getdoc(@PathVariable Long id,@PathVariable String filename) {
        Members member =  service.getMemberById(id);
        Optional<Document> optional = member.getDocuments()
        				.stream()
        				.filter(object->object.getFilename().equals(filename))
        				.findFirst();
        return optional.get();
    }
}
