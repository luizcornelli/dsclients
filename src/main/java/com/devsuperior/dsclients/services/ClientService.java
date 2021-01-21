package com.devsuperior.dsclients.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsclients.dto.ClientDTO;
import com.devsuperior.dsclients.entities.Client;
import com.devsuperior.dsclients.repositories.ClientRepository;
import com.devsuperior.dsclients.services.exceptions.DatabaseException;
import com.devsuperior.dsclients.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsclients.services.exceptions.UtilsValidationException;
import com.devsuperior.dsclients.services.utils.Format;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {

		Page<Client> list = clientRepository.findAll(pageRequest);

		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {

		Optional<Client> optional = clientRepository.findById(id);
		Client client = optional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO insert(ClientDTO clientDTO) {

		try {
			
			Client client = new Client();
			
			client.setName(clientDTO.getName());
			client.setCpf(Format.formatCpf(clientDTO.getCpf()));
			client.setIncome(clientDTO.getIncome());
			client.setBirthDate(clientDTO.getBirthDate());
			client.setChildren(clientDTO.getChildren());
			
			client = clientRepository.save(client);

			return new ClientDTO(client);
		} catch (UtilsValidationException e) {
			throw new UtilsValidationException("Not format cpf " + clientDTO.getCpf());
		}
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO clientDTO) {

		try {

			Client client = clientRepository.getOne(id);

			client.setName(clientDTO.getName());
			client.setCpf(Format.formatCpf(clientDTO.getCpf()));
			client.setIncome(clientDTO.getIncome());
			client.setBirthDate(clientDTO.getBirthDate());
			client.setChildren(clientDTO.getChildren());
			
			client = clientRepository.save(client);

			return new ClientDTO(client);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (UtilsValidationException e) {
			throw new UtilsValidationException("Not format cpf " + clientDTO.getCpf());
		}
	}

	public void delete(Long id) {

		try {

			clientRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {

			throw new ResourceNotFoundException("Id not found " + id);

		} catch (DataIntegrityViolationException e) {

			throw new DatabaseException("Integrity violation");
		}
	}
}
