package libreria.com.libwill.controller;

import libreria.com.libwill.dto.request.CreatePedidoOnlineDTO;
import libreria.com.libwill.dto.request.UpdateEstadoPedidoDTO;
import libreria.com.libwill.dto.response.PedidoOnlineResponseDTO;
import libreria.com.libwill.service.PedidoOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoOnlineController {

    private final PedidoOnlineService service;

    // Crear pedido (cliente)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoOnlineResponseDTO> crearPedido(
            @RequestBody CreatePedidoOnlineDTO dto,
            Principal principal) {

        String username = principal.getName();
        return ResponseEntity.ok(service.crearPedido(dto, username));
    }

    // Ver mis pedidos (cliente)
    @GetMapping("/mis-pedidos")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PedidoOnlineResponseDTO>> obtenerMisPedidos(
            Principal principal) {

        String username = principal.getName();
        return ResponseEntity.ok(service.obtenerMisPedidos(username));
    }

    // Ver pedido por id
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoOnlineResponseDTO> obtenerPorId(
            @PathVariable Long id,
            Principal principal) {

        String username = principal.getName();
        return ResponseEntity.ok(service.obtenerPedidoPorId(id, username));
    }

    // Cancelar pedido (solo dueño del pedido)
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoOnlineResponseDTO> cancelarPedido(
            @PathVariable Long id,
            Principal principal) {

        String username = principal.getName();
        return ResponseEntity.ok(service.cancelarPedido(id, username));
    }

    // Cambiar estado (admin/vendedor)
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<PedidoOnlineResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody UpdateEstadoPedidoDTO dto,
            Principal principal) {

        String username = principal.getName();
        return ResponseEntity.ok(service.actualizarEstado(id, dto, username));
    }

    // Listar todos los pedidos (admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoOnlineResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodosPedidos());
    }

    // Pedidos por estado (admin/vendedor)
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<List<PedidoOnlineResponseDTO>> obtenerPedidosPorEstado(
            @PathVariable String estado) {

        return ResponseEntity.ok(service.obtenerPedidosPorEstado(
                Enum.valueOf(libreria.com.libwill.entity.enums.EstadoPedido.class, estado)
        ));
    }
}
