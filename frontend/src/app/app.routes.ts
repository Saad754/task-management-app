import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { TaskForm } from './pages/task-form/task-form';
import { Register } from './pages/register/register';
import { Tasks } from './pages/tasks/tasks';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'task-form', component: TaskForm },
  { path: 'task-form/:id', component: TaskForm },
  { path: 'register', component: Register },
  { path: 'tasks', component: Tasks },
  { path: '**', redirectTo: 'login' }
];
