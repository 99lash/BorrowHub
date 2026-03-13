<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
use App\Repositories\Interfaces\UserRepositoryInterface;
use App\Repositories\Eloquent\UserRepository;
use App\Repositories\Interfaces\CategoryRepositoryInterface;
use App\Repositories\Eloquent\CategoryRepository;
use App\Repositories\Interfaces\ItemRepositoryInterface;
use App\Repositories\Eloquent\ItemRepository;

class AppServiceProvider extends ServiceProvider
{
    /**
     * Register any application services.
     */
    public function register(): void
    {
        // Auth 
        $this->app->bind(
            \App\Repositories\Interfaces\UserRepositoryInterface::class,
            \App\Repositories\Eloquent\UserRepository::class
        );

        // Student 
        $this->app->bind(
            \App\Repositories\Interfaces\StudentRepositoryInterface::class,
            \App\Repositories\Eloquent\EloquentStudentRepository::class
        );

        // Inventory 
        $this->app->bind(
            \App\Repositories\Interfaces\CategoryRepositoryInterface::class,
            \App\Repositories\Eloquent\CategoryRepository::class
        );
        $this->app->bind(
            \App\Repositories\Interfaces\ItemRepositoryInterface::class,
            \App\Repositories\Eloquent\ItemRepository::class
        );
    }

    /**
     * Bootstrap any application services.
     */
    public function boot(): void
    {
        //
    }
}
