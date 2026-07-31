import { Component, signal, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private auth = inject(Auth);
  private router = inject(Router);

  username = signal('');
  password = signal('');
  error = signal('');

  onLogin(): void {
    this.auth.login({
      username: this.username(),
      password: this.password()
    }).subscribe({
      next: (response) => {
        this.auth.saveToken(response.token);
        this.router.navigate(['/tasks']);
      },
      error: () => {
        this.error.set('Invalid username or password');
      }
    });
  }
}
