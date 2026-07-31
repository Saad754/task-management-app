import { Component, signal, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-register',
  imports: [FormsModule,RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  private auth = inject(Auth);
  private router = inject(Router);

  username = signal('');
  email = signal('');
  password = signal('');
  error = signal('');

  onRegister(): void {
    this.auth.register({
      username: this.username(),
      email: this.email(),
      password: this.password()
    }).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.error.set(err.error?.error || 'Registration failed');
      }
    });
  }
}
